package github.dmitmel.raketaframework.web.server;

import github.dmitmel.raketaframework.util.IterableUtils;
import github.dmitmel.raketaframework.util.NetUtils;
import github.dmitmel.raketaframework.util.ReflectionUtils;
import github.dmitmel.raketaframework.util.exceptions.InvocationTargetException;
import github.dmitmel.raketaframework.web.HTTPDateFormatter;
import github.dmitmel.raketaframework.web.HTTPRequest;
import github.dmitmel.raketaframework.web.HTTPResponse;
import github.dmitmel.raketaframework.web.URL;
import github.dmitmel.raketaframework.web.errors.Error404;
import github.dmitmel.raketaframework.web.errors.Error405;
import github.dmitmel.raketaframework.web.errors.Error500;
import github.dmitmel.raketaframework.web.errors.HTTPError;
import github.dmitmel.raketaframework.web.handle.Document;
import github.dmitmel.raketaframework.web.handle.RedirectionThrowable;
import github.dmitmel.raketaframework.web.handle.RequestData;
import github.dmitmel.raketaframework.web.handle.WebFormData;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;

class ClientHandler implements Runnable {
    private Server server;
    private Socket socket;
    private Matcher urlPatternMatcher;
    private HTTPRequest incomingMessage;

    private int responseStatusCode;
    private String responseStatusDescription;
    private HashMap<String, String> responseHeaders = new HashMap<>(0);
    private String responseBody;

    private OutputStream outputStream;
    private InputStream inputStream;

    public ClientHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            addInitialResponseHeaders();
            incomingMessage = getIncomingMessage();
            URLMapping.HandlerData currentHandlerData = getHandlerDataForURL(incomingMessage.url);

            if (currentHandlerData == null) {
                addErrorDataToResponse(new Error404());

            } else if (!currentHandlerData.supportsMethod(incomingMessage.method)) {
                addErrorDataToResponse(new Error405());

                addAllowedMethodsToResponseHeadersFromHandlerData(currentHandlerData);

            } else if (incomingMessage.method.equals("OPTIONS")) {
                addAllowedMethodsToResponseHeadersFromHandlerData(currentHandlerData);

            } else {
                executeHandleMethodFromData(currentHandlerData);
            }

            /* Wikipedia says this about HTTP header "Date"
            (https://en.wikipedia.org/wiki/List_of_HTTP_header_fields):

     	    "The date and time that the message was originated (in "HTTP-date" format as defined by RFC 7231
     	    Date/Time Formats)"

     	    So, now message is being generated, and date must be specified NOW. Also, if handle method took many time
     	    to execute - date added before execution wouldn't be valid.
             */
            DateFormat df = DateFormat.getTimeInstance();
            df.setTimeZone(TimeZone.getTimeZone("GMT"));
            responseHeaders.put("Date", df.format(new Date()));

            HTTPResponse outgoingMessage = new HTTPResponse("HTTP/1.1", responseStatusCode, responseStatusDescription,
                    responseHeaders, responseBody);

            outputStream.write(outgoingMessage.toString().getBytes());
        } catch (IOException e) {
            server.logger.exception(e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            printRequest();
        }
    }

    private void addInitialResponseHeaders() {
        responseHeaders.put("Server", server.serverName);
    }

    private HTTPRequest getIncomingMessage() throws IOException {
        String receivedRequestString = readRequestFromInputStream(inputStream);
        return HTTPRequest.fromRequestString(receivedRequestString);
    }

    private String readRequestFromInputStream(InputStream input) throws IOException {
        // Buffer with size 64 KB
        byte buffer[] = new byte[64 * 1024];
        int receivedLength = input.read(buffer);
        return new String(buffer, 0, receivedLength);
    }

    private URLMapping.HandlerData getHandlerDataForURL(URL url) {
        for (URLMapping.HandlerData handlerData : server.urls) {
            Matcher matcher = handlerData.urlPattern.matcher(url.path);
            if (matcher.matches()) {
                urlPatternMatcher = matcher;
                return handlerData;
            }
        }

        return null;
    }

    private void addAllowedMethodsToResponseHeadersFromHandlerData(URLMapping.HandlerData currentHandlerData) {
        Set<String> supportedMethodsNames = currentHandlerData.supportedMethods.keySet();
        responseHeaders.put("Allow", IterableUtils.join(supportedMethodsNames, ", "));
    }

    private void addRedirectDataToResponse(String targetUrl) {
        responseStatusCode = 303;
        responseStatusDescription = "See Other";
        responseHeaders.put("Location", targetUrl);
    }

    private void addErrorDataToResponse(HTTPError httpError) {
        Document errorDocument = new Document();
        server.errorResponders.get(httpError.getCode()).makeResponseDocument(errorDocument, httpError);

        responseStatusCode = httpError.getCode();
        responseStatusDescription = httpError.getDescription();
        responseHeaders.put("Content-Type", errorDocument.getMimeType());
        responseHeaders.put("Connection", "close");
        responseHeaders.put("Pragma", "no-cache");
        responseBody = errorDocument.getData();
    }

    private void executeHandleMethodFromData(URLMapping.HandlerData currentHandlerData) {
        try {
            Document document = new Document();
            RequestData requestData = new RequestData(urlPatternMatcher, incomingMessage);
            Method handleMethod =
                    currentHandlerData.supportedMethods.get(incomingMessage.method);

            if (requestData.isForm()) {
                ReflectionUtils.invokeMethodInAnyCase(handleMethod, currentHandlerData.requestHandlerInstance,
                        WebFormData.castFrom(requestData), document);
            } else {
                ReflectionUtils.invokeMethodInAnyCase(handleMethod, currentHandlerData.requestHandlerInstance,
                        requestData, document);
            }

            responseStatusCode = 200;
            responseStatusDescription = "OK";
            responseHeaders.put("Content-Length", Integer.toString(document.getData().length()));
            responseHeaders.put("Content-Type", document.getMimeType());
            responseHeaders.put("Connection", "close");
            responseBody = document.getData();
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();

            if (cause instanceof HTTPError) {
                switch (((HTTPError) cause).getCode()) {
                    case 500:   // 500 Internal Server Error
                        throw new UnsupportedOperationException(
                                String.format("throw %s in request handler methods",
                                        Error500.class.getTypeName()));

                    case 501:   // 501 Not Implemented
                        addAllowedMethodsToResponseHeadersFromHandlerData(currentHandlerData);
                    default:
                        addErrorDataToResponse((HTTPError) cause);
                        break;
                }
            } else if (cause instanceof RedirectionThrowable) {
                addRedirectDataToResponse(((RedirectionThrowable) cause).targetUrl);
            } else {
                addErrorDataToResponse(new Error500());
                server.logger.exception(e);
            }

        } catch (Exception e) {
            addErrorDataToResponse(new Error500());
            server.logger.exception(e);
        }
    }

    private void printRequest() {
        InetSocketAddress inetSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();

        String message = String.format("%s - - [%s] \"%s %s %s\" - %s %s",
                NetUtils.inetSocketAddressToString(inetSocketAddress),
                HTTPDateFormatter.formatCurrentDate(),
                incomingMessage.protocol, incomingMessage.method, incomingMessage.url.path,
                responseStatusCode, responseStatusDescription);
        server.logger.log(LoggingLevel.REQUEST_SUMMARY, message);
    }
}

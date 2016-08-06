package org.willthisfly.raketaframework.server;

import org.willthisfly.raketaframework.*;
import org.willthisfly.raketaframework.errors.*;
import org.willthisfly.raketaframework.routes.Route;
import org.willthisfly.raketaframework.routes.Router;
import org.willthisfly.raketaframework.util.Streams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ClientHandler implements Runnable {
    private final Server server;
    private final Socket clientSocket;
    private int requestLength;
    private HTTPRequest incomingMessage;

    private int responseStatusCode;
    private String responseStatusDescription;
    private Map<String, String> responseHeaders = new HashMap<>();
    private byte[] responseBody;
    private HTTPResponse outgoingMessage;
    
    public ClientHandler(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();
            
            incomingMessage = getIncomingMessageFromInputStream(inputStream);
            
            Router router = server.router;
            Route route = router.routeForHTTPRequest(incomingMessage);

            if (!route.allowsURI(incomingMessage.uri)) {
                addErrorDataToResponse(new Error404());
            } else if (!route.allowsMethod(incomingMessage.method)) {
                addErrorDataToResponse(new Error405());
                addAllowedMethodsToResponseHeaders(router.methodsForURI(incomingMessage.uri));
            } else if (incomingMessage.method.equals("OPTIONS")) {
                addAllowedMethodsToResponseHeaders(router.methodsForURI(incomingMessage.uri));
            } else {
                executeHandlerFromRoute(route);
            }
    
            responseHeaders.put("Server", server.serverName);
            responseHeaders.put("Connection", "close");
            /* Wikipedia says this about HTTP header "Date"
            (https://en.wikipedia.org/wiki/List_of_HTTP_header_fields):

     	    "The date and time that the message was originated (in "HTTP-date" format as defined by RFC 7231
     	    Date/Time Formats)"

     	    So, now message is being generated, and date must be specified NOW. Also, if handle method took many time
     	    to execute - date added before execution wouldn't be valid.
             */
            responseHeaders.put("Date", HTTPDateFormatter.currentDateInGMTFormat());
    
            outgoingMessage = new HTTPResponse("HTTP/1.1", responseStatusCode, responseStatusDescription,
                    responseHeaders, responseBody);

            outputStream.write(outgoingMessage.getBytes());
        } catch (Exception e) {
            server.logger.exception(e);
        } finally {
            Streams.closeQuietly(clientSocket);

            printRequest();
        }
    }

    private HTTPRequest getIncomingMessageFromInputStream(InputStream inputStream) throws IOException {
        byte[] receivedRequest = readRequestFromInputStream(inputStream);
        return HTTPRequest.fromBytes(receivedRequest);
    }
    
    private byte[] readRequestFromInputStream(InputStream inputStream) throws IOException {
        // Creating buffer with size of 64 KB
        byte[] buffer = new byte[1024 * 64];
        this.requestLength = inputStream.read(buffer);
        
        byte[] trimmedBuffer = new byte[requestLength];
        System.arraycopy(buffer, 0, trimmedBuffer, 0, requestLength);
        
        return trimmedBuffer;
    }
    
    private void printRequest() {
        HTTPRequest incomingMessage = this.incomingMessage;
        if (incomingMessage == null)
            incomingMessage = new HTTPRequest("-", new URI("-"), "HTTP/1.1");
        
        InetSocketAddress inetSocketAddress = (InetSocketAddress) clientSocket.getRemoteSocketAddress();
    
        server.logger.log(LoggingLevel.SUMMARY, "%s -> %s, got %d bytes, sent %d bytes",
                inetSocketAddress.getHostString(),
                incomingMessage.uri, requestLength, outgoingMessage.length());
        server.logger.log(LoggingLevel.SUMMARY, "%s - - [%s] \"%s %s %s\" - %s %s",
                inetSocketAddress.getHostString(),
                HTTPDateFormatter.currentDateInServerLogFormat(),
                incomingMessage.protocol, incomingMessage.method, incomingMessage.uri.path,
                responseStatusCode, responseStatusDescription);
    }
    
    private void addAllowedMethodsToResponseHeaders(List<String> methods) {
        responseHeaders.put("Allow", String.join(", ", methods));
    }
    
    private void addErrorDataToResponse(HTTPError httpError) {
        ErrorResponder responder = server.errorResponders.get(httpError.getCode());
        Object result = responder.makeResponse();
        
        HTTPResponse errorResponseAdditions = handlingResultToHTTPResponse(result);

        responseStatusCode = httpError.getCode();
        responseStatusDescription = httpError.getDescription();
        responseHeaders.put("Pragma", "no-cache");
        responseHeaders.putAll(errorResponseAdditions.headers);
        responseBody = errorResponseAdditions.body;
    }

    private void executeHandlerFromRoute(Route route) {
        try {
            RequestParams params = new RequestParams(incomingMessage);
            List<String> captures = route.getCapturesForURI(incomingMessage.uri);
            Map<String, String> namedCaptures = route.getNamedCapturesForURI(incomingMessage.uri);
            params.setupCaptures(captures, namedCaptures);
            
            Object result = route.handleRequest(params);
            HTTPResponse responseAdditions = handlingResultToHTTPResponse(result);
            
            responseStatusCode = responseAdditions.statusCode;
            responseStatusDescription = responseAdditions.statusDescription;
            responseHeaders.putAll(responseAdditions.headers);
            responseBody = responseAdditions.body;
            
        } catch (Exception e) {
            if (e instanceof HTTPError) {
                HTTPError httpError = (HTTPError) e;
                
                switch (httpError.getCode()) {
                    case 500:   // 500 Internal Server Error
                        throw new UnsupportedOperationException("throw Error500 in request handlers");

                    case 501:   // 501 Not Implemented
                        addAllowedMethodsToResponseHeaders(server.router.methodsForURI(incomingMessage.uri));
                    default:
                        addErrorDataToResponse(httpError);
                        break;
                }
            } else {
                addErrorDataToResponse(new Error500());
                server.logger.exception(e);
            }
        }
    }
    
    private HTTPResponse handlingResultToHTTPResponse(Object result) {
        int statusCode;
        String statusDescription;
        HashMap<String, String> headers = new HashMap<>();
        byte[] body = HTTPMessage.EMPTY_BODY;
        
        if (result instanceof HTTPResponse) {
            HTTPResponse httpResponse = (HTTPResponse) result;
        
            statusCode = httpResponse.statusCode;
            statusDescription = httpResponse.statusDescription;
            headers.putAll(httpResponse.headers);
            body = httpResponse.body;
        } else if (result instanceof Redirect) {
            Redirect redirect = (Redirect) result;
            
            statusCode = 303;
            statusDescription = "See Other";
            headers.put("Location", redirect.targetUrl);
        } else {
            int contentLength;
            String contentType;
            
            if (result instanceof String) {
                String string = (String) result;
                
                contentLength = string.length();
                contentType = MIMETypes.getContentTypeOrElse(string.getBytes(), MIMETypes.PLAIN_TEXT);
                body = string.getBytes();
            } else if (result instanceof byte[]) {
                byte[] bytes = (byte[]) result;
                
                contentLength = bytes.length;
                contentType = MIMETypes.getContentTypeOrElse(bytes, MIMETypes.BYTE_STREAM);
                body = bytes;
            } else if (result instanceof Document) {
                Document document = (Document) result;
                
                contentLength = document.length();
                contentType = document.mimeType;
                body = document.getBytes();
            } else if (result instanceof StringBuilder) {
                StringBuilder stringBuilder = (StringBuilder) result;
                byte[] bytes = stringBuilder.toString().getBytes();
                
                contentLength = stringBuilder.length();
                contentType = MIMETypes.getContentTypeOrElse(bytes, MIMETypes.PLAIN_TEXT);
                body = bytes;
            } else if (result instanceof List) {
                List iterable = (List) result;
    
                Document document = new Document();
                for (Object item : iterable)
                    document.println(item.toString());
                
                contentLength = document.length();
                contentType = MIMETypes.getContentTypeOrElse(document.getBytes(), MIMETypes.PLAIN_TEXT);
                body = document.getBytes();
            } else {
                String string = result.toString();
                
                contentLength = string.length();
                contentType = MIMETypes.getContentTypeOrElse(string.getBytes(), MIMETypes.PLAIN_TEXT);
                body = string.getBytes();
            }
    
            statusCode = 200;
            statusDescription = "OK";
            
            headers.put("Content-Length", Integer.toString(contentLength));
            headers.put("Content-Type", contentType);
        }
    
        return new HTTPResponse("HTTP/1.1", statusCode, statusDescription, headers, body);
    }
}

package github.dmitmel.raketaframework.web.server;

import github.dmitmel.raketaframework.pinelog.Logger;
import github.dmitmel.raketaframework.util.NetUtils;
import github.dmitmel.raketaframework.web.errors.DefaultErrorResponderFactory;
import github.dmitmel.raketaframework.web.errors.ErrorResponder;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class Server {
    public static final int DEFAULT_PORT = 8080;
    public static final String DEFAULT_HOST_NAME = "jonnect-server";

    public final URLMapping urls;

    /** First part of web address (before semicolon). */
    public final String ip;
    public final int port;

    /** Value, that will be used in HTTP-header "Server". Default value is {@link #DEFAULT_HOST_NAME}. */
    public final String serverName;

    public Map<Integer, ErrorResponder> errorResponders = new HashMap<>(0);

    final Logger logger = new Logger(Server.class.getTypeName());

    public Server(URLMapping urls) {
        this(urls, "localhost", DEFAULT_PORT, DEFAULT_HOST_NAME);
    }
    public Server(URLMapping urls, String ip, int port) {
        this(urls, ip, port, DEFAULT_HOST_NAME);
    }
    public Server(URLMapping urls, String ip, int port, String serverName) {
        this.urls = urls;
        this.port = port;
        this.ip = ip;
        this.serverName = serverName;
        this.errorResponders.put(404, DefaultErrorResponderFactory.makeResponder()); // 404 Not Found
        this.errorResponders.put(405, DefaultErrorResponderFactory.makeResponder()); // 405 Method Not Allowed
        this.errorResponders.put(500, DefaultErrorResponderFactory.makeResponder()); // 500 Internal Server Error
        this.errorResponders.put(501, DefaultErrorResponderFactory.makeResponder()); // 501 Not Implemented
    }

    public void startServer() {
        try {
            logger.info("Starting server...");

            logger.debug("Initializing server socket...");
            ServerSocket serverSocket = initServerSocket();

            logger.info(String.format("Server started on address http://%s:%d/", ip, port));
            logger.debug(String.format("Listening on port %d...", port));
            while (true) {
                Socket clientSocket = serverSocket.accept();
                acceptClient(clientSocket);
            }
        } catch (java.io.IOException e) {
            // Can be caught only on accepting socket
            // Just ignore this request. If someone stopped request - he/she/it can retry or don't do anything
        } catch (Exception e) {
            logger.fatal("Server stopped because of uncaught exception.");
            logger.exception(e);
        }
    }

    private ServerSocket initServerSocket() {
        try {
            return new ServerSocket(port, 0, InetAddress.getByName(ip));

        } catch (BindException e) {
            if (e.getMessage().startsWith("Cannot assign requested address"))
                throw new UnAssignableAddressException(ip, e);

            else if (e.getMessage().startsWith("Address already in use"))
                throw new PortAlreadyInUseException(port, e);

            else
                throw new RuntimeException("unknown bind exception was caught while starting server", e);

        } catch (java.net.UnknownHostException e) {
            throw github.dmitmel.raketaframework.util.exceptions.UnknownHostException.extractFrom(e);

        } catch (java.io.IOException e) {
            throw github.dmitmel.raketaframework.util.exceptions.IOException.extractFrom(e);
        }
    }
    
    private void acceptClient(Socket clientSocket) {
        InetSocketAddress inetSocketAddress = (InetSocketAddress) clientSocket.getRemoteSocketAddress();
        logger.debug(String.format("Starting processing request from %s",
                NetUtils.inetSocketAddressToString(inetSocketAddress)));

        Thread acceptorThread = new Thread(new ClientHandler(this, clientSocket));
        acceptorThread.setPriority(Thread.NORM_PRIORITY);
        acceptorThread.setDaemon(true);

        int acceptorIndex = Thread.activeCount() - 1;
        String threadName = String.format("%s-%d", ClientHandler.class.getTypeName(), acceptorIndex);
        acceptorThread.setName(threadName);

        acceptorThread.start();
    }

}

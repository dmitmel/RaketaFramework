package github.dmitmel.raketaframework.web.server;

import github.dmitmel.raketaframework.Version;
import github.dmitmel.raketaframework.util.NetUtils;
import github.dmitmel.raketaframework.web.errors.DefaultErrorResponderFactory;
import github.dmitmel.raketaframework.web.errors.ErrorResponder;

import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class Server {
    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 8080;
    public static final String DEFAULT_HOST_NAME = String.format("RaketaFramework/%s on %s %s",
            Version.id(), System.getProperty("os.name"), System.getProperty("os.version"));
    public static final int DEFAULT_MAX_ACTIVE_THREADS_COUNT = 10;

    public final URLMapping urls;

    public final String host;
    public final int port;

    /** Value, that will be used in HTTP-header "Server". Default value is {@link #DEFAULT_HOST_NAME}. */
    public String serverName = DEFAULT_HOST_NAME;

    public Map<Integer, ErrorResponder> errorResponders = new HashMap<>(0);

    final ServerLogger logger = new ServerLogger();
    public void setLogLevelFilterer(Predicate<LoggingLevel> filterer) {
        logger.logLevelFilterer = filterer;
    }

    public int maxActiveThreads = DEFAULT_MAX_ACTIVE_THREADS_COUNT;
    private int activeThreads;

    public Server(URLMapping urls) {
        this(urls, DEFAULT_HOST, DEFAULT_PORT);
    }
    public Server(URLMapping urls, String host, int port) {
        this.urls = urls;
        this.port = port;
        this.host = host;
        this.errorResponders.put(404, DefaultErrorResponderFactory.makeResponder()); // 404 Not Found
        this.errorResponders.put(405, DefaultErrorResponderFactory.makeResponder()); // 405 Method Not Allowed
        this.errorResponders.put(500, DefaultErrorResponderFactory.makeResponder()); // 500 Internal Server Error
        this.errorResponders.put(501, DefaultErrorResponderFactory.makeResponder()); // 501 Not Implemented
    }

    public void start() {
        try {
            logger.log(LoggingLevel.INFO, "Starting server...");

            logger.log(LoggingLevel.STARTING_CONFIG, "Initializing server socket...");
            ServerSocket serverSocket = initServerSocket();

            logger.log(LoggingLevel.INFO, String.format("Server started on address http://%s:%d/", host, port));
            while (true) {
                Socket clientSocket = serverSocket.accept();
                acceptClient(clientSocket);
            }
        } catch (java.io.IOException e) {
            // Can be caught only on accepting socket
            // Just ignore this request. If someone stopped request - he/she/it can retry or don't do anything
        } catch (Exception e) {
            logger.log(LoggingLevel.FATAL_ERROR, "Server stopped because of uncaught exception.");
            logger.exception(e);
        }
    }

    private ServerSocket initServerSocket() {
        try {
            return new ServerSocket(port, 0, InetAddress.getByName(host));

        } catch (BindException e) {
            if (e.getMessage().startsWith("Cannot assign requested address"))
                throw new UnAssignableAddressException(host, e);

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
        waitUntilOpportunityToStartThread();
        activeThreads++;

        InetSocketAddress inetSocketAddress = (InetSocketAddress) clientSocket.getRemoteSocketAddress();
        logger.log(LoggingLevel.CLIENT_ACCEPTING_START, String.format("Starting processing request from %s",
                NetUtils.inetSocketAddressToString(inetSocketAddress)));

        Thread handlerThread = new Thread(() -> {
            new ClientHandler(this, clientSocket).run();
            activeThreads--;
            logger.log(LoggingLevel.CLIENT_ACCEPTING_END, String.format("Finished processing request from %s",
                    NetUtils.inetSocketAddressToString(inetSocketAddress)));
        });
        handlerThread.setPriority(Thread.NORM_PRIORITY);
        handlerThread.setDaemon(true);

        int acceptorIndex = activeThreads - 1;
        String threadName = String.format("%s-%d", ClientHandler.class.getTypeName(), acceptorIndex);
        handlerThread.setName(threadName);

        handlerThread.start();
    }

    private void waitUntilOpportunityToStartThread() {
        while (activeThreads >= maxActiveThreads);
    }

}

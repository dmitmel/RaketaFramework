package org.willthisfly.raketaframework.server;

import org.willthisfly.raketaframework.Version;
import org.willthisfly.raketaframework.errors.DefaultErrorResponderMapMaker;
import org.willthisfly.raketaframework.errors.ErrorResponder;
import org.willthisfly.raketaframework.routes.Router;
import org.willthisfly.raketaframework.util.Streams;
import org.willthisfly.raketaframework.exceptions.*;

import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class Server {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8080;
    private static final String DEFAULT_SERVER_NAME = String.format("RaketaFramework/%s on %s %s",
            Version.id(), System.getProperty("os.name"), System.getProperty("os.version"));
    private static final int DEFAULT_MAX_ACTIVE_THREADS_COUNT = 10;

    public final Router router;

    public final String host;
    public final int port;

    /** Value, that will be used in HTTP-header "Server". Default value is {@link #DEFAULT_SERVER_NAME}. */
    public String serverName = DEFAULT_SERVER_NAME;

    public Map<Integer, ErrorResponder> errorResponders = new HashMap<>();

    final ServerLogger logger = new ServerLogger();
    public void setLogLevelFilterer(Predicate<LoggingLevel> filterer) {
        logger.logLevelFilterer = filterer;
    }

    public int maxActiveThreads = DEFAULT_MAX_ACTIVE_THREADS_COUNT;
    private int activeThreads;
    
    private ServerSocket serverSocket;
    
    public boolean stopped = false;

    public Server(Router router) {
        this(router, DEFAULT_HOST, DEFAULT_PORT);
    }
    public Server(Router router, String host, int port) {
        this.router = router;
        this.port = port;
        this.host = host;
        this.errorResponders = DefaultErrorResponderMapMaker.makeMap();
    }

    public void start() {
        try {
            logger.log(LoggingLevel.INFO, "Starting server...");

            logger.log(LoggingLevel.STARTING_CONFIG, "Initializing server socket...");
            serverSocket = initServerSocket();
            
            logger.log(LoggingLevel.STARTING_CONFIG, "Initializing shutdown hooks...");
            initShutdownHooks();

            logger.log(LoggingLevel.INFO, "Server started on address http://%s:%d/\n", host, port);
            
            while (!stopped) {
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
                throw new UnknownAddressException(host, e);

            else if (e.getMessage().startsWith("Address already in use"))
                throw new PortIsListeningException(port, e);

            else
                throw new RuntimeException("unknown bind exception while starting server", e);

        } catch (java.net.UnknownHostException e) {
            throw org.willthisfly.raketaframework.exceptions.UnknownHostException.extractFrom(e);

        } catch (java.io.IOException e) {
            throw IOException.extractFrom(e);
        }
    }
    
    private void initShutdownHooks() {
        Thread serverStopperShutdownHook = new Thread(this::stop);
        serverStopperShutdownHook.setName("ServerStopperShutdownHook");
        serverStopperShutdownHook.setPriority(Thread.MAX_PRIORITY);
        Runtime.getRuntime().addShutdownHook(serverStopperShutdownHook);
    }
    
    public void stop() {
        System.err.println();
    
        logger.log(LoggingLevel.INFO, "Stopping server...");
        stopped = true;
    
        logger.log(LoggingLevel.STOPPING_CONFIG, "Closing server socket...");
        Streams.closeQuietly(serverSocket);
    
        logger.log(LoggingLevel.INFO, "Server successfully stopped!");
    }
    
    private void acceptClient(Socket clientSocket) {
        waitUntilOpportunityToStartThread();

        InetSocketAddress inetSocketAddress = (InetSocketAddress) clientSocket.getRemoteSocketAddress();
        String clientHostString = inetSocketAddress.getHostString();
        logger.log(LoggingLevel.CLIENT_ACCEPTING_START, "Starting processing request from %s", clientHostString);

        Thread handlerThread = new Thread(() -> {
            long startTime = System.currentTimeMillis();
            
            try {
                new ClientHandler(this, clientSocket).run();
            } finally {
                activeThreads--;
                logger.log(LoggingLevel.CLIENT_ACCEPTING_END, "Finished processing request from %s", clientHostString);
                
                long currentTime = System.currentTimeMillis();
                logger.log(LoggingLevel.SUMMARY, "Request took %d ms", currentTime - startTime);
    
                System.err.println();
            }
        });
        
        handlerThread.setPriority(Thread.NORM_PRIORITY);
        handlerThread.setDaemon(true);
        String threadName = String.format("HandlerThread-%d", activeThreads);
        handlerThread.setName(threadName);

        handlerThread.start();
        activeThreads++;
    }

    private void waitUntilOpportunityToStartThread() {
        while (activeThreads >= maxActiveThreads);
    }
}

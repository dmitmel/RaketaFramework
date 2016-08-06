package org.willthisfly.raketaframework.testingapp;

import org.willthisfly.raketaframework.errors.Error404;
import org.willthisfly.raketaframework.routes.BlockBasedRouter;
import org.willthisfly.raketaframework.server.Server;
import org.willthisfly.raketaframework.util.Streams;

import java.io.InputStream;
import java.util.regex.Pattern;

public class Main {
    private static final String HOST = "localhost";
    private static final int PORT = 8015;
    
    public static final String URL = String.format("http://%s:%d", HOST, PORT);
    
    private static final int LONG_TASK_TIMEOUT = 1000;
    
    public static void main(String[] args) {
        BlockBasedRouter routes = new BlockBasedRouter();
    
        routes.get("/long-task", params -> {
            sleep(LONG_TASK_TIMEOUT);
            return "Success!";
        });
    
        routes.get("/file/:path", params -> {
            String path = "/" + params.get("path");
            InputStream inputStream = Main.class.getResourceAsStream(path);
            if (inputStream == null)
                throw new Error404();
            return Streams.readAllWithClosing(inputStream);
        });
    
        routes.get("/hello", params -> {
            StringBuilder builder = new StringBuilder();

            String greeting = params.getOrElse("greeting", "Hello");
            String who = params.getOrElse("who", "World");
            int howMany = Integer.parseInt(params.getOrElse("how-many", "1"));

            for (int i = 0; i < howMany; i++)
                builder.append(greeting).append(", ").append(who).append("!\n");

            return builder;
        });
    
        routes.get(Pattern.compile("/regexp/say/(?<what>.+?)/to/(?<who>.+)"), params -> {
            String what = params.get("what");
            String who = params.get("who");
            return String.format("%s, %s!", what, who);
        });
    
        routes.get("/glob/say/:what/to/:who", params -> {
            String greeting = params.get("what");
            String who = params.get("who");
            return String.format("%s, %s!", greeting, who);
        });
    
    
        Server server = new Server(routes, HOST, PORT);
        server.start();
    }
    
    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

package github.dmitmel.raketaframework.tests;

import github.dmitmel.raketaframework.util.StreamUtils;
import github.dmitmel.raketaframework.web.errors.Error404;
import github.dmitmel.raketaframework.web.handle.*;
import github.dmitmel.raketaframework.web.server.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TestApplication {
    public static final String HOST = "localhost";
    public static final int PORT = 8015;
    
    public static final String URL = String.format("http://%s:%d", HOST, PORT);
    
    public static final int LONG_TASK_TIMEOUT = 1000;
    
    public static void main(String[] args) {
        try {
            HandlersList handlersList = new HandlersList(new LongTaskHandler(),
                    new FileHandler(), new HelloGeneratorHandler());
            Server server = new Server(handlersList, HOST, PORT);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @RequestURLPattern("/long-task/?")
    private static class LongTaskHandler implements RequestHandler {
        @RequestMethod("GET")
        public String GET(RequestData requestData) throws InterruptedException {
            Thread.sleep(LONG_TASK_TIMEOUT);
            return "Success!";
        }
    }
    
    @RequestURLPattern("/file/(.+)")
    private static class FileHandler implements RequestHandler {
        @RequestMethod("GET")
        public byte[] GET(RequestData requestData) {
            String path = '/' + requestData.getMatcherGroup(0);
            InputStream inputStream = TestApplication.class.getResourceAsStream(path);
            if (inputStream == null)
                throw new Error404();
            return StreamUtils.readAll(inputStream);
        }
    }
    
    @RequestURLPattern("/hello-generator/?")
    public static class HelloGeneratorHandler implements RequestHandler {
        @RequestMethod("GET")
        public StringBuilder GET(RequestData requestData) {
            StringBuilder builder = new StringBuilder(0);
            
            String greeting = requestData.getUrlParamOrElse("greeting", "Hello");
            String who = requestData.getUrlParamOrElse("who", "World");
            int howMany = Integer.parseInt(requestData.getUrlParamOrElse("how-many", "1"));
    
            for (int i = 0; i < howMany; i++)
                builder.append(greeting).append(", ").append(who).append("!\n");
            
            return builder;
        }
    }
}

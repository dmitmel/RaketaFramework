package github.dmitmel.raketaframework.tests;

import github.dmitmel.raketaframework.app.RequestParams;
import github.dmitmel.raketaframework.errors.Error404;
import github.dmitmel.raketaframework.routes.AddMethodRoute;
import github.dmitmel.raketaframework.routes.BlockBasedRouter;
import github.dmitmel.raketaframework.routes.MethodBasedRouter;
import github.dmitmel.raketaframework.server.Server;
import github.dmitmel.raketaframework.util.Streams;

import java.io.InputStream;

public class TestApplication {
    public static final String HOST = "localhost";
    public static final int PORT = 8015;
    
    public static final String URL = String.format("http://%s:%d", HOST, PORT);
    
    public static final int LONG_TASK_TIMEOUT = 1000;
    
    public static void main(String[] args) {
        try {
            BlockBasedRouter routes = new BlockBasedRouter();

            routes.get("/long-task", params -> {
                sleep(LONG_TASK_TIMEOUT);
                return "Success!";
            });

            routes.get("/file/:path", params -> {
                String path = '/' + params.getString("path");
                InputStream inputStream = TestApplication.class.getResourceAsStream(path);
                if (inputStream == null)
                    throw new Error404();
                return Streams.readAllWithClosing(inputStream);
            });

            routes.get("/hello", params -> {
                StringBuilder builder = new StringBuilder();

                String greeting = params.getStringOrElse("greeting", "Hello");
                String who = params.getStringOrElse("who", "World");
                int howMany = Integer.parseInt(params.getStringOrElse("how-many", "1"));

                for (int i = 0; i < howMany; i++)
                    builder.append(greeting).append(", ").append(who).append("!\n");

                return builder;
            });
    
            MethodBasedRouter additionalRouter = new MethodBasedRouter();
            additionalRouter.addRoute(TestApplication.class, "sayHelloToSomeoneUsingRegexp");
            routes.addAll(additionalRouter);
            
            routes.get("/glob/say/:what/to/:who", params -> {
                String greeting = params.getString("what");
                String who = params.getString("who");
                return String.format("%s, %s!", greeting, who);
            });
            
            routes.post("/echo", params -> params.getRequest().body);
            
            
            Server server = new Server(routes, HOST, PORT);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    
    @AddMethodRoute("/regexp/say/(?<what>.+?)/to/(?<who>.+)")
    private static String sayHelloToSomeoneUsingRegexp(RequestParams params, String what, String who) {
        return String.format("%s, %s!", what, who);
    }
}

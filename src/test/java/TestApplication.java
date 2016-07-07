import github.dmitmel.raketaframework.web.handle.*;
import github.dmitmel.raketaframework.web.server.*;

public class TestApplication {
    public static void main(String[] args) {
        HandlersList handlersList = new HandlersList(new HelloHandler(), new Greeter());
        Server server = new Server(handlersList);
        server.start();
    }

    @RequestURLPattern("/hello")
    private static class HelloHandler implements RequestHandler {
        @RequestMethod("GET")
        private String GET(RequestData requestData) {
            return "Hello World!";
        }
    }

    @RequestURLPattern("/greet")
    private static class Greeter implements RequestHandler {
        @RequestMethod("GET")
        private Document GET(RequestData requestData) {
            Document document = new Document();

            String who = requestData.getUrlParamOrElse("who", "World");
            int howMany = Integer.parseInt(requestData.getUrlParamOrElse("how-many", "1"));

            for (int i = 0; i < howMany; i++) {
                document.writeF("Hello, %s!\r\n", who);
            }

            return document;
        }
    }
}

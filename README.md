# RaketaFramework

RaketaFramework is a framework for creating JVM-based web apps. Here's an example:

```java
import github.dmitmel.raketaframework.util.MoreFiles;
import github.dmitmel.raketaframework.web.MimeTypes;
import github.dmitmel.raketaframework.web.errors.Error404;
import github.dmitmel.raketaframework.web.handle.*;
import github.dmitmel.raketaframework.web.server.*;

public class TestServer {
    public static void main(String[] args) {
        URLMapping urls = new URLMapping(new Main(), new Hello(), new GreetForm(), new RedirectPage());

        Server app = new Server(urls, "localhost", 8015, "RaketaServer1");
        app.startServer();
    }

    // "Hello World!" example
    @RequestURLPattern("/main")
    private static class Main implements RequestHandler {
        @RequestMethod("GET")
        public void GET(RequestData requestData, Document document) {
            document.writeln("Hello World!");
        }
    }

    // Handling requests with parameters example
    @RequestURLPattern("/hello")
    private static class Hello implements RequestHandler {
        @RequestMethod("GET")
        public void GET(RequestData requestData, Document document) {
            document.setMimeType(MimeTypes.HTML_DOCUMENT);
            
            String name = requestData.getUrlParamOrElse("name", "World");
            
            String howManyStr = requestData.getUrlParam("how-many");
            int howMany = 1;
            if (howManyStr != null) {
                howMany = Integer.parseInt(howManyStr);
            }
            
            document.writeln("<html>\r\n\t<body>\r\n\t\t<tt>\r\n\r\n\t\t\t<!-- Just repeat same thing many times! " +
                    "(Generated HTML) -->\r\n");
            for (int i = 0; i < howMany; i++) {
                document.writeln(String.format("\t\t\tHello %s!<br>", name));
            }
            
            document.write("\r\n\t\t</tt>\r\n\t</body>\r\n</html>");
        }
    }
    
    // Form example
    @RequestURLPattern("/greet")
    private static class GreetForm implements RequestHandler {
        @RequestMethod("GET")
        public void GET(RequestData requestData, Document document) {
            document.setMimeType(MimeTypes.HTML_DOCUMENT);
            // File ../resources/greet-form.html contains such HTML:
            /*
            <h2>Hello, User!<br></h2>
            <h3>After you'll use this thing,<br>please, tell me some data about you!</h3>
            <blockquote style="border: 3px dashed #777; padding: 20px; width: 20%; margin-top: 70px;">
                <form action="/greet" method="POST">
                    <p>What's your name?</p>
                    <p><input type="text" name="name" value="World" autocomplete="off"></p>
                    <p style="margin-top: 40px;">What greeting would you like<br>to have? (e.g. "Hi", "Hello")</p>
                    <p><input type="text" name="greet" value="Hello" autocomplete="off"></p>
                    <p><input type="submit" value="Submit"></p>
                </form>
            </blockquote>
            */
            document.writeln(MoreFiles.load(MoreFiles.realPath("../resources/greet-form.html")));
        }

        @RequestMethod("POST")
        public void POST(WebFormData form, Document document) {
            if (form.getFormParam("name") == null) throw new Error404();
            if (form.getFormParam("greet") == null) throw new Error404();
            
            document.writeF("%s %s!", form.getFormParam("greet"), form.getFormParam("name"));
        }
    }
    
    // Redirect example
    @RequestURLPattern("/redirect")
    private static class RedirectPage implements RequestHandler {
        @RequestMethod("GET")
        public void GET(RequestData requestData, Document document) {
            throw new RedirectingThrowable("/main");
        }
    }
}

```

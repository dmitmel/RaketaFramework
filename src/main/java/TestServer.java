import github.dmitmel.raketaframework.util.MoreFiles;
import github.dmitmel.raketaframework.web.MimeTypes;
import github.dmitmel.raketaframework.web.errors.Error404;
import github.dmitmel.raketaframework.web.handle.*;
import github.dmitmel.raketaframework.web.handle.RedirectingThrowable;
import github.dmitmel.raketaframework.web.server.URLMapping;
import github.dmitmel.raketaframework.web.server.Server;

import java.util.HashMap;

public class TestServer {
    private static HashMap<String, String> files = new HashMap<>();
    
    public static void main(String[] args) {
        for (String file : new String[] {"../resources/greet-form.html",
                "../resources/goodies/css/glowing-style.css",
                "../resources/index-js.js",
                "../resources/goodies/js-libs/jquery.min.js",
                "../resources/index.html"}) {
            files.put(file.substring(13, file.length()), MoreFiles.load(MoreFiles.realPath(file)));
        }
        
        URLMapping urls = new URLMapping(new Main(), new Hello(), new LoadFile(), new GreetForm(),
                new GetLoadedFile(), new LongTask(), new RedirectPage());

        Server app = new Server(urls, "localhost", 8015, "RaketaServer1");
        app.startServer();
    }

    @RequestURLPattern("/main")
    private static class Main implements RequestHandler {
        @RequestMethod("GET")
        public void GET(RequestData requestData, Document document) {
            document.setMimeType(MimeTypes.HTML_DOCUMENT);
            document.writeln(files.get("index.html"));
        }
    }

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

    @RequestURLPattern("/load-file/(.+)")
    private static class LoadFile implements RequestHandler {
        @RequestMethod("GET")
        public void GET(RequestData requestData, Document document) {
            String lines;
            try {
                lines = MoreFiles.load(MoreFiles.realPath("../resources/" + requestData.getMatcherGroup(0)));
                document.setMimeType(MimeTypes.getApproximateTypeFor(requestData.getMatcherGroup(0)));
                
            } catch (NullPointerException e) {
                throw new Error404();
            }
            
            document.writeln(lines);
        }
    }

    @RequestURLPattern("/greet")
    private static class GreetForm implements RequestHandler {
        @RequestMethod("GET")
        public void GET(RequestData requestData, Document document) {
            document.setMimeType(MimeTypes.HTML_DOCUMENT);
            document.writeln(files.get("greet-form.html"));
        }

        @RequestMethod("POST")
        public void POST(WebFormData form, Document document) {
            if (form.getFormParam("name") == null) throw new Error404();
            if (form.getFormParam("greet") == null) throw new Error404();
            
            document.writeF("%s %s!", form.getFormParam("greet"), form.getFormParam("name"));
        }
    }

    @RequestURLPattern("/get-loaded/(.+)")
    private static class GetLoadedFile implements RequestHandler {
        @RequestMethod("GET")
        public void GET(RequestData requestData, Document document) {
            if (!files.containsKey(requestData.getMatcherGroup(0))) throw new Error404();
            
            document.setMimeType(MimeTypes.getApproximateTypeFor(requestData.getMatcherGroup(0)));
            
            document.writeln(files.get(requestData.getMatcherGroup(0)));
        }
    }

    @RequestURLPattern("/long-task")
    private static class LongTask implements RequestHandler {
        @RequestMethod("GET")
        public void GET(RequestData requestData, Document document) throws InterruptedException {
            Thread.sleep(5000);
            document.writeln("Finished!");
        }
    }

    @RequestURLPattern("/redirect")
    private static class RedirectPage implements RequestHandler {
        @RequestMethod("GET")
        public void GET(RequestData requestData, Document document) {
            throw new RedirectingThrowable("/main");
        }
    }
}

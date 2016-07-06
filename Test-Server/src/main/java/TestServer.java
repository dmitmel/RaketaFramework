import github.dmitmel.raketaframework.util.MoreFiles;
import github.dmitmel.raketaframework.util.NetUtils;
import github.dmitmel.raketaframework.util.exceptions.FileNotFoundException;
import github.dmitmel.raketaframework.web.MIMETypes;
import github.dmitmel.raketaframework.web.errors.Error404;
import github.dmitmel.raketaframework.web.handle.*;
import github.dmitmel.raketaframework.web.server.Server;
import github.dmitmel.raketaframework.web.server.URLMapping;

public class TestServer {
    public static void main(String[] args) {
        URLMapping urls = new URLMapping(new Main(), new Hello(), new LoadFile(), new GreetForm());

        Server app = new Server(urls, NetUtils.inetAddressToString(NetUtils.getCurrentSiteLocalIP()), 8015);
        app.start();
    }

    @RequestURLPattern("/main")
    private static class Main implements RequestHandler {
        @RequestMethod("GET")
        public String GET(RequestData requestData) {
            throw new RedirectionThrowable("/load/index.html");
        }
    }

    @RequestURLPattern("/hello")
    private static class Hello implements RequestHandler {
        @RequestMethod("GET")
        public Document GET(RequestData requestData) {
            Document document = new Document();
            document.mimeType = MIMETypes.HTML_DOCUMENT;

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

            return document;
        }
    }

    @RequestURLPattern("/load/(.+)")
    private static class LoadFile implements RequestHandler {
        @RequestMethod("GET")
        public byte[] GET(RequestData requestData) {
            try {
                return MoreFiles.readBytes(MoreFiles.realPath("../resources/" + requestData.getMatcherGroup(0)));
            } catch (FileNotFoundException e) {
                throw new Error404();
            }
        }
    }

    @RequestURLPattern("/greet")
    private static class GreetForm implements RequestHandler {
        @RequestMethod("GET")
        public String  GET(RequestData requestData) {
            throw new RedirectionThrowable("/load/greet-form.html");
        }

        @RequestMethod("POST")
        public String POST(WebFormData form) {
            if (form.getFormParam("name") == null) throw new Error404();
            if (form.getFormParam("greet") == null) throw new Error404();

            return String.format("%s %s!", form.getFormParam("greet"), form.getFormParam("name"));
        }
    }
}

import github.dmitmel.raketaframework.util.MoreFiles;
import github.dmitmel.raketaframework.util.NetUtils;
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
        public void GET(RequestData requestData, Document document) {
            throw new RedirectionThrowable("/load/index.html");
        }
    }

    @RequestURLPattern("/hello")
    private static class Hello implements RequestHandler {
        @RequestMethod("GET")
        public void GET(RequestData requestData, Document document) {
            document.setMimeType(MIMETypes.HTML_DOCUMENT);

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

    @RequestURLPattern("/load/(.+)")
    private static class LoadFile implements RequestHandler {
        @RequestMethod("GET")
        public void GET(RequestData requestData, Document document) {
            try {
                String lines = MoreFiles.load(MoreFiles.realPath("./" + requestData.getMatcherGroup(0)));
                document.setMimeType(MIMETypes.getContentType(requestData.getMatcherGroup(0)));
                document.write(lines);
            } catch (NullPointerException e) {
                throw new Error404();
            }
        }
    }

    @RequestURLPattern("/greet")
    private static class GreetForm implements RequestHandler {
        @RequestMethod("GET")
        public void GET(RequestData requestData, Document document) {
            throw new RedirectionThrowable("/load/greet-form.html");
        }

        @RequestMethod("POST")
        public void POST(WebFormData form, Document document) {
            if (form.getFormParam("name") == null) throw new Error404();
            if (form.getFormParam("greet") == null) throw new Error404();

            document.writeF("%s %s!", form.getFormParam("greet"), form.getFormParam("name"));
        }
    }
}

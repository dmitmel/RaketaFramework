package github.dmitmel.raketaframework.web.handle;

/**
 * You can throw this exception in {@link github.dmitmel.raketaframework.web.handle.RequestHandler} to redirect to
 * another page.
 */
public class RedirectionThrowable extends RuntimeException {
    public final String targetUrl;

    public RedirectionThrowable(String targetUrl) {
        super();
        this.targetUrl = targetUrl;
    }
}

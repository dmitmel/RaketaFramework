package github.dmitmel.raketaframework.web.handle;

/**
 * You can throw this exception in {@link github.dmitmel.raketaframework.web.handle.RequestHandler} to redirect to
 * another page.
 */
public class RedirectingThrowable extends RuntimeException {
    public final String targetUrl;

    public RedirectingThrowable(String targetUrl) {
        super();
        this.targetUrl = targetUrl;
    }
}

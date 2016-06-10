package github.dmitmel.raketaframework.web.errors;

/**
 * Factory for default responders. Before adding factory, all default responders had such structure:
 *
 * <pre><code>
 * package github.dmitmel.raketaframework.web.errors;
 *
 * import github.dmitmel.raketaframework.web.handle.Document;
 *
 * public class DefaultError<font color="orange">[[[some state]]]</font>Responder implements ErrorResponder {
 * &#x40;Override
 *     public void makeResponseDocument(Document document) {
 *         document.writeln("<font color="orange">[[[some state]]]</font> <font color="orange">[[[some description]]]</font>");
 *     }
 * }
 * </code></pre>
 *
 * Now, I can do this thing by only returning such thing:
 *
 * <pre><code>
 * (document, httpError) -> document.writeln(httpError.toString())
 * </code></pre>
 */
public class DefaultErrorResponderFactory {
    private DefaultErrorResponderFactory() {

    }

    public static ErrorResponder makeResponder() {
        return (document, httpError) -> document.writeln(httpError.toString());
    }
}

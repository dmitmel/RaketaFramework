package github.dmitmel.raketaframework.web.handle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation specifies on what web-method Java-method of {@link RequestHandler} will be used. This methods:
 *
 * <ol>
 *     <li>can be not accessible for
 *         {@link github.dmitmel.raketaframework.web.server.ClientHandler}</li>
 *     <li>if they receive form, they must have 1 param with type {@link WebFormData}</li>
 *     <li>if they receive simple request, they must have 1 param with type {@link RequestData}</li>
 *     <li>They can return this types:
 *         <ol>
 *             <li>{@link String}</li>
 *             <li>{@code byte[]}</li>
 *             <li>{@link Document}</li>
 *             <li>{@link StringBuilder}</li>
 *         </ol>
 *     </li>
 * </ol>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestMethod {
    String value();
}

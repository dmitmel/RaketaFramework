package github.dmitmel.raketaframework.web.handle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation specifies on what web-method Java-method of {@link RequestHandler} will be used. This methods:
 *
 * <ul>
 *     <li>can be not accessible for
 *     {@link github.dmitmel.raketaframework.web.server.ClientHandler}</li>
 *     <li>if they receive form, they must have 2 arguments:
 *         <ul>
 *             <li>{@link WebFormData}</li>
 *             <li>{@link Document}</li>
 *         </ul>
 *     </li>
 *     <li>if they receive simple request, they must have 2 arguments:
 *         <ul>
 *             <li>{@link RequestData}</li>
 *             <li>{@link Document}</li>
 *         </ul>
 *     </li>
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestMethod {
    String value();
}

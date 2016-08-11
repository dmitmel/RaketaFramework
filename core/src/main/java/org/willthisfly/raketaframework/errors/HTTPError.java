package org.willthisfly.raketaframework.errors;

import org.willthisfly.raketaframework.util.ExtendedComparator;

import java.util.Objects;

/**
 * You can throw subclass of this class (<code>ErrorXXX</code>) in handler to send HTTP
 * error as result.
 *
 * <h3>Example</h3>
 *
 * <pre><code>
 * public class App {
 *     public static void main(String[] args) {
 *         BlockBasedRouter router = new BlockBasedRouter();
 *         router.get("/:path", params -&gt; {
 *             // Adding slash to load resource from classpath
 *             String path = "/" + params.get("path");
 *             InputStream inputStream = App.class.getResourceAsStream(path);
 *
 *             if (inputStream == null)
 *                 throw new Error404();
 *             else
 *                 return Streams.readAllWithClosing(inputStream);
 *         });
 *
 *         Server server = new Server(router);
 *         server.start();
 *     }
 * }
 * </code></pre>
 */
public abstract class HTTPError extends RuntimeException implements Comparable<HTTPError>, Cloneable {
    public HTTPError() {
        super();
    }
    
    public abstract int getCode();
    public abstract String getDescription();
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof HTTPError))
            return false;
        
        HTTPError that = (HTTPError) o;
        return this.getCode() == that.getCode() &&
                Objects.equals(this.getDescription(), that.getDescription());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getDescription());
    }
    
    @Override
    public HTTPError clone() {
        try {
            return (HTTPError) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new UnsupportedOperationException("HTTPError.clone");
        }
    }
    
    @Override
    public String toString() {
        return String.format("%s %s", getCode(), getDescription());
    }
    
    @Override
    public int compareTo(HTTPError that) {
        return ExtendedComparator.compareComparingResults(
                Integer.compare(this.getCode(), that.getCode()),
                ExtendedComparator.compareNullable(this.getDescription(), that.getDescription())
        );
    }
}

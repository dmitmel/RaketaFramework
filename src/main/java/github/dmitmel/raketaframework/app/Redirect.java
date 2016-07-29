package github.dmitmel.raketaframework.app;

import github.dmitmel.raketaframework.util.ExtendedComparator;

import java.util.Objects;

/**
 * You can return instance of this class to redirect to another page.
 */
public class Redirect implements Comparable<Redirect>, Cloneable {
    public final String targetUrl;

    public Redirect(String targetUrl) {
        this.targetUrl = targetUrl;
    }
    
    @Override
    public String toString() {
        return targetUrl;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Redirect))
            return false;
        
        Redirect that = (Redirect) o;
        return Objects.equals(this.targetUrl, that.targetUrl);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(targetUrl);
    }
    
    @Override
    public Redirect clone() {
        try {
            return (Redirect) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("unreachable code");
        }
    }
    
    @Override
    public int compareTo(Redirect that) {
        return ExtendedComparator.compareNullable(this.targetUrl, that.targetUrl);
    }
}

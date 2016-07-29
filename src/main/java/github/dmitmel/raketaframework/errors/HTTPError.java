package github.dmitmel.raketaframework.errors;

import github.dmitmel.raketaframework.util.ExtendedComparator;

import java.util.Objects;

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
            throw new RuntimeException("unreachable code");
        }
    }
    
    @Override
    public String toString() {
        return String.format("%s %s", getCode(), getDescription());
    }
    
    @Override
    public int compareTo(HTTPError that) {
        int i = Integer.compare(this.getCode(), that.getCode());
        if (i != ExtendedComparator.EQUALS)
            return i;
        
        return ExtendedComparator.compareNullable(this.getDescription(), that.getDescription());
    }
}

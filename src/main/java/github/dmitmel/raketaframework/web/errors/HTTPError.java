package github.dmitmel.raketaframework.web.errors;

public abstract class HTTPError extends RuntimeException {
    public HTTPError() {
        super();
    }

    public abstract int getCode();
    public abstract String getDescription();

    @Override
    public String toString() {
        return String.format("%s %s", getCode(), getDescription());
    }
}

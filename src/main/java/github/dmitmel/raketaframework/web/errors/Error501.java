package github.dmitmel.raketaframework.web.errors;

public class Error501 extends HTTPError {
    @Override
    public int getCode() {
        return 501;
    }

    @Override
    public String getDescription() {
        return "Not Implemented";
    }
}

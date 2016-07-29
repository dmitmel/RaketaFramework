package github.dmitmel.raketaframework.errors;

public class Error501 extends HTTPError {
    public Error501() {
        super();
    }

    @Override
    public int getCode() {
        return 501;
    }

    @Override
    public String getDescription() {
        return "Not Implemented";
    }
}

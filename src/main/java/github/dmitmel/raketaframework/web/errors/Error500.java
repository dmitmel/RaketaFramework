package github.dmitmel.raketaframework.web.errors;

public class Error500 extends HTTPError {
    public Error500() {
        super();
    }

    @Override
    public int getCode() {
        return 500;
    }

    @Override
    public String getDescription() {
        return "Internal Server Error";
    }
}

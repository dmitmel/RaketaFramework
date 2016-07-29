package org.willthisfly.raketaframework.errors;

public class Error404 extends HTTPError {
    public Error404() {
        super();
    }

    @Override
    public int getCode() {
        return 404;
    }

    @Override
    public String getDescription() {
        return "Not Found";
    }
}

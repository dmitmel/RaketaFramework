package org.willthisfly.raketaframework.errors;

public class Error400 extends HTTPError {
    public Error400() {
        super();
    }

    @Override
    public int getCode() {
        return 400;
    }

    @Override
    public String getDescription() {
        return "Bad Request";
    }
}

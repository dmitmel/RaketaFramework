package github.dmitmel.raketaframework.web.handle;

public class NoHandleMethodsInHandlerException extends RuntimeException {
    private Class<? extends RequestHandler> handlerClass;
    public Class<? extends RequestHandler> getHandlerClass() {
        return handlerClass;
    }

    public NoHandleMethodsInHandlerException(Class<? extends RequestHandler> handlerClass) {
        super(handlerClass.getTypeName());
        this.handlerClass = handlerClass;
    }
}

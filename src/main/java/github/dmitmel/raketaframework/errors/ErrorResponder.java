package github.dmitmel.raketaframework.errors;

/**
 * Class for error responders. Error responders are being activated on special error codes in
 * {@link github.dmitmel.raketaframework.server.ClientHandler}, and receive document to build
 * error response.
 */
public interface ErrorResponder {
    Object makeResponse();
}

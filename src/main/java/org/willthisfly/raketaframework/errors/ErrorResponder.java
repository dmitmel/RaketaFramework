package org.willthisfly.raketaframework.errors;

/**
 * Class for error responders. Error responders are being
 * activated on special error codes in
 * {@link org.willthisfly.raketaframework.server.ClientHandler}.
 */
public interface ErrorResponder {
    Object makeResponse();
}

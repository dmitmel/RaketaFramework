package github.dmitmel.raketaframework.web.errors;

import github.dmitmel.raketaframework.web.handle.Document;

/**
 * Class for error responders. Error responders are being activated on special error codes in
 * {@link github.dmitmel.raketaframework.web.server.ClientHandler}, and receive document to build error text.
 */
public interface ErrorResponder {
    void makeResponseDocument(Document document, HTTPError httpError);
}

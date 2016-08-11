package org.willthisfly.raketaframework.errors;

/**
 * Class for error responders. Error responders are being
 * activated on special error codes to make responses.
 */
@FunctionalInterface
public interface ErrorResponder {
    Object makeResponse();
}

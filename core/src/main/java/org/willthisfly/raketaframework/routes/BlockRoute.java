package org.willthisfly.raketaframework.routes;

import org.willthisfly.raketaframework.RequestParams;

public interface BlockRoute<R> extends Route {
    @Override
    default Object handleRequest(RequestParams params) {
        return getHandlerBlock().handleRequest(params);
    }
    
    Block<R> getHandlerBlock();
    
    @FunctionalInterface
    interface Block<T> {
        T handleRequest(RequestParams params);
    }
}

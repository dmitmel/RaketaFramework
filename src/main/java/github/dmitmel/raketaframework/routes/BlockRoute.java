package github.dmitmel.raketaframework.routes;

import github.dmitmel.raketaframework.app.RequestParams;

public interface BlockRoute extends Route {
    @Override
    default Object handleRequest(RequestParams params) {
        return getHandlerBlock().handleRequest(params);
    }
    
    Block getHandlerBlock();
    
    @FunctionalInterface
    interface Block {
        Object handleRequest(RequestParams params);
    }
}

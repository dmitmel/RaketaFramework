package org.willthisfly.raketaframework.routes;

import org.willthisfly.raketaframework.util.GlobParser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Router for making <a href="http://www.sinatrarb.com/">Sinatra</a>-like applications.
 *
 * <h3>Example</h3>
 *
 * <pre><code>
 * public class App {
 *     public static void main(String[] args) {
 *         BlockBasedRouter router = new BlockBasedRouter();
 *         router.get("/hello", params -&gt; "Hello World!");
 *
 *         Server server = new Server(router);
 *         server.start();
 *     }
 * }
 * </code></pre>
 */
public class BlockBasedRouter extends Router {
    private final ArrayList<org.willthisfly.raketaframework.routes.Route> routes = new ArrayList<>();
    
    private <T> BlockBasedRouter addRoute(String method, Pattern pattern, BlockRoute.Block<T> handlerBlock) {
        routes.add(new BlockBasedRouter.Route<>(method, pattern, handlerBlock));
        return this;
    }
    
    @Override
    public BlockBasedRouter addAll(Router other) {
        this.routes.addAll(other.getRoutes());
        return this;
    }
    
    @Override
    public List<org.willthisfly.raketaframework.routes.Route> getRoutes() {
        return routes;
    }
    
    
    ///////////////////////// ADDING ROUTES WITH REGEXPS /////////////////////////
    
    public <T> BlockBasedRouter get(Pattern regexp, BlockRoute.Block<T> handlerBlock) {
        return addRoute("GET", regexp, handlerBlock);
    }
    
    public <T> BlockBasedRouter post(Pattern regexp, BlockRoute.Block<T> handlerBlock) {
        return addRoute("POST", regexp, handlerBlock);
    }
    
    public <T> BlockBasedRouter put(Pattern regexp, BlockRoute.Block<T> handlerBlock) {
        return addRoute("PUT", regexp, handlerBlock);
    }
    
    public <T> BlockBasedRouter patch(Pattern regexp, BlockRoute.Block<T> handlerBlock) {
        return addRoute("PATCH", regexp, handlerBlock);
    }
    
    public <T> BlockBasedRouter update(Pattern regexp, BlockRoute.Block<T> handlerBlock) {
        return addRoute("UPDATE", regexp, handlerBlock);
    }
    
    public <T> BlockBasedRouter delete(Pattern regexp, BlockRoute.Block<T> handlerBlock) {
        return addRoute("DELETE", regexp, handlerBlock);
    }
    
    
    ///////////////////////// ADDING ROUTES WITH GLOBS /////////////////////////
    
    public <T> BlockBasedRouter get(String glob, BlockRoute.Block<T> handlerBlock) {
        return addRoute("GET", GlobParser.toPatternWithCaching(glob), handlerBlock);
    }
    
    public <T> BlockBasedRouter post(String glob, BlockRoute.Block<T> handlerBlock) {
        return addRoute("POST", GlobParser.toPatternWithCaching(glob), handlerBlock);
    }
    
    public <T> BlockBasedRouter put(String glob, BlockRoute.Block<T> handlerBlock) {
        return addRoute("PUT", GlobParser.toPatternWithCaching(glob), handlerBlock);
    }
    
    public <T> BlockBasedRouter patch(String glob, BlockRoute.Block<T> handlerBlock) {
        return addRoute("PATCH", GlobParser.toPatternWithCaching(glob), handlerBlock);
    }
    
    public <T> BlockBasedRouter update(String glob, BlockRoute.Block<T> handlerBlock) {
        return addRoute("UPDATE", GlobParser.toPatternWithCaching(glob), handlerBlock);
    }
    
    public <T> BlockBasedRouter delete(String glob, BlockRoute.Block<T> handlerBlock) {
        return addRoute("DELETE", GlobParser.toPatternWithCaching(glob), handlerBlock);
    }
    
    
    private class Route<T> implements BlockRoute<T>, PatternRoute {
        private final String method;
        private final Pattern pattern;
        private final BlockRoute.Block<T> handlerBlock;
        
        public Route(String method, Pattern pattern, BlockRoute.Block<T> handlerBlock) {
            this.method = method;
            this.pattern = pattern;
            this.handlerBlock = handlerBlock;
        }
    
        @Override
        public String getMethod() {
            return method;
        }
    
        @Override
        public Pattern getPattern() {
            return pattern;
        }
    
        @Override
        public Block<T> getHandlerBlock() {
            return handlerBlock;
        }
        
        @Override
        public Route clone() {
            try {
                return (Route) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new UnsupportedOperationException("BlockBasedRouter.Block.clone");
            }
        }
    }
}

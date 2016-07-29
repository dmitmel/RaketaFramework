package github.dmitmel.raketaframework.routes;

import github.dmitmel.raketaframework.util.GlobParser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class BlockBasedRouter extends Router {
    private final ArrayList<github.dmitmel.raketaframework.routes.Route> routes = new ArrayList<>();
    
    private void addRoute(String method, Pattern pattern, BlockRoute.Block handlerBlock) {
        routes.add(new BlockBasedRouter.Route(method, pattern, handlerBlock));
    }
    
    public void addAll(Router other) {
        this.routes.addAll(other.getRoutes());
    }
    
    @Override
    public List<github.dmitmel.raketaframework.routes.Route> getRoutes() {
        return routes;
    }
    
    
    ///////////////////////// ADDING ROUTES WITH REGEXPS /////////////////////////
    
    public void get(Pattern regexp, BlockRoute.Block handlerBlock) {
        addRoute("GET", regexp, handlerBlock);
    }
    
    public void post(Pattern regexp, BlockRoute.Block handlerBlock) {
        addRoute("POST", regexp, handlerBlock);
    }
    
    public void put(Pattern regexp, BlockRoute.Block handlerBlock) {
        addRoute("PUT", regexp, handlerBlock);
    }
    
    public void patch(Pattern regexp, BlockRoute.Block handlerBlock) {
        addRoute("PATCH", regexp, handlerBlock);
    }
    
    public void update(Pattern regexp, BlockRoute.Block handlerBlock) {
        addRoute("UPDATE", regexp, handlerBlock);
    }
    
    public void delete(Pattern regexp, BlockRoute.Block handlerBlock) {
        addRoute("DELETE", regexp, handlerBlock);
    }
    
    
    ///////////////////////// ADDING ROUTES WITH GLOBS /////////////////////////
    
    public void get(String glob, BlockRoute.Block handlerBlock) {
        addRoute("GET", GlobParser.globToPatternWithCaching(glob), handlerBlock);
    }
    
    public void post(String glob, BlockRoute.Block handlerBlock) {
        addRoute("POST", GlobParser.globToPatternWithCaching(glob), handlerBlock);
    }
    
    public void put(String glob, BlockRoute.Block handlerBlock) {
        addRoute("PUT", GlobParser.globToPatternWithCaching(glob), handlerBlock);
    }
    
    public void patch(String glob, BlockRoute.Block handlerBlock) {
        addRoute("PATCH", GlobParser.globToPatternWithCaching(glob), handlerBlock);
    }
    
    public void update(String glob, BlockRoute.Block handlerBlock) {
        addRoute("UPDATE", GlobParser.globToPatternWithCaching(glob), handlerBlock);
    }
    
    public void delete(String glob, BlockRoute.Block handlerBlock) {
        addRoute("DELETE", GlobParser.globToPatternWithCaching(glob), handlerBlock);
    }
    
    
    private class Route implements BlockRoute, PatternRoute {
        private final String method;
        private final Pattern pattern;
        private final BlockRoute.Block handlerBlock;
        
        public Route(String method, Pattern pattern, BlockRoute.Block handlerBlock) {
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
        public Block getHandlerBlock() {
            return handlerBlock;
        }
    }
}

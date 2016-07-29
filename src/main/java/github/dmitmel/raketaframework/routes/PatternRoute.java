package github.dmitmel.raketaframework.routes;

import github.dmitmel.raketaframework.URI;
import github.dmitmel.raketaframework.util.Patterns;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface PatternRoute extends Route {
    @Override
    default boolean allowsURI(URI uri) {
        Matcher matcher = Patterns.matcherWithCaching(getPattern(), uri.path);
        return matcher.matches();
    }
    
    Pattern getPattern();
    
    @Override
    default List<String> getCapturesForURI(URI uri) {
        Matcher matcher = Patterns.matcherWithCaching(getPattern(), uri.path);
        
        if (matcher.matches()) {
            ArrayList<String> groups = new ArrayList<>();
            
            for (int i = 1; i <= matcher.groupCount(); i++)
                groups.add(matcher.group(i));
            
            return Collections.unmodifiableList(groups);
        } else {
            return Collections.emptyList();
        }
    }
    
    @Override
    default Map<String, String> getNamedCapturesForURI(URI uri) {
        Matcher matcher = Patterns.matcherWithCaching(getPattern(), uri.path);
        
        if (matcher.matches()) {
            String[] groupsNames = Patterns.getNamedGroupsNames(matcher);
            HashMap<String, String> groups = new HashMap<>(groupsNames.length);
            
            for (String groupName : groupsNames)
                groups.put(groupName, matcher.group(groupName));
            
            return Collections.unmodifiableMap(groups);
        } else {
            return Collections.emptyMap();
        }
    }
}

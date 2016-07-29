package github.dmitmel.raketaframework.tests.util;

import github.dmitmel.raketaframework.util.Patterns;
import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternTest extends Assert {
    @Test
    public void testSingleUsedGroup() {
        Matcher matcher = Pattern.compile("(?<username>.+?)@.+").matcher("test@example.com");
        
        if (matcher.matches()) {
            String[] groups = Patterns.getNamedGroupsNames(matcher);
            String[] expected = {"username"};
            assertArrayEquals(expected, groups);
        } else {
            fail();
        }
    }
    
    @Test
    public void testMultipleUsedGroups() {
        Matcher matcher = Pattern.compile("(?<username>.+?)@(?<site>.+)").matcher("test@example.com");
        
        if (matcher.matches()) {
            String[] groups = Patterns.getNamedGroupsNames(matcher);
            String[] expected = {"username", "site"};
            assertArrayEquals(expected, groups);
        } else {
            fail();
        }
    }
    
    @Test
    public void testSingleUnusedGroup() {
        Matcher matcher = Pattern.compile("(?<choiceA>\\d+)|(?<choiceB>[a-zA-Z]+)").matcher("choiceB");
        
        if (matcher.matches()) {
            String[] groups = Patterns.getNamedGroupsNames(matcher);
            String[] expected = {"choiceB"};
            assertArrayEquals(expected, groups);
        } else {
            fail();
        }
    }
    
    @Test
    public void testMultipleUnusedGroups() {
        Matcher matcher = Pattern.compile("(?<choiceA1>\\d+)" +
                                          "(?<choiceA2>[a-zA-Z]+)" +
                                          "|" +
                                          "(?<choiceB1>[a-zA-Z]+)" +
                                          "(?<choiceB2>\\d+)").matcher("123ABC");
        
        if (matcher.matches()) {
            String[] groups = Patterns.getNamedGroupsNames(matcher);
            String[] expected = {"choiceA1", "choiceA2"};
            assertArrayEquals(expected, groups);
        } else {
            fail();
        }
    }
}

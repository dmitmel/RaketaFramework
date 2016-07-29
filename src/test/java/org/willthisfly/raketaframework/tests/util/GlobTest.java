package org.willthisfly.raketaframework.tests.util;

import org.willthisfly.raketaframework.util.GlobParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class GlobTest extends Assert {
    @Test
    public void testZeroOrMoreCharSelector() {
        Pattern pattern = GlobParser.globToPatternWithCaching("*.txt");
        assertTrue(pattern.matcher("abc.txt").matches());
        assertTrue(pattern.matcher(".txt").matches());
    }
    
    @Test
    public void testGroups() {
        Pattern pattern = GlobParser.globToPatternWithCaching("{code,libs,books}.html");
        assertTrue(pattern.matcher("code.html").matches());
        assertTrue(pattern.matcher("libs.html").matches());
        assertTrue(pattern.matcher("books.html").matches());
        assertFalse(pattern.matcher("abc.html").matches());
        assertFalse(pattern.matcher(".html").matches());
    }
    
    @Test
    public void testPathSeparators() {
        Pattern pattern = GlobParser.globToPatternWithCaching("/say/**/to/*");
        assertTrue(pattern.matcher("/say/hello/to/world").matches());
        assertTrue(pattern.matcher("/say/hi/to/").matches());
        assertFalse(pattern.matcher("/say/hi/to").matches());
        assertTrue(pattern.matcher("/say//to/world").matches());
        assertFalse(pattern.matcher("/say/to/world").matches());
    }
    
    @Test
    public void testAnyCharSelector() {
        Pattern pattern = GlobParser.globToPatternWithCaching("file?.txt");
        assertTrue(pattern.matcher("file1.txt").matches());
        assertTrue(pattern.matcher("files.txt").matches());
        assertTrue(pattern.matcher("file_.txt").matches());
        assertFalse(pattern.matcher("file10.txt").matches());
        assertFalse(pattern.matcher("file.txt").matches());
    }
    
    @Test
    public void testCharClass() {
        Pattern pattern = GlobParser.globToPatternWithCaching("[abc].txt");
        assertTrue(pattern.matcher("a.txt").matches());
        assertTrue(pattern.matcher("b.txt").matches());
        assertTrue(pattern.matcher("c.txt").matches());
        assertFalse(pattern.matcher("d.txt").matches());
        assertFalse(pattern.matcher(".txt").matches());
    }
    
    @Test
    public void testRangeInCharClass() {
        Pattern pattern = GlobParser.globToPatternWithCaching("[a-c].txt");
        assertTrue(pattern.matcher("a.txt").matches());
        assertTrue(pattern.matcher("b.txt").matches());
        assertTrue(pattern.matcher("c.txt").matches());
        assertFalse(pattern.matcher("d.txt").matches());
        assertFalse(pattern.matcher(".txt").matches());
    }
    
    @Test
    public void testExcludedCharClass() {
        Pattern pattern = GlobParser.globToPatternWithCaching("[!abc].txt");
        assertFalse(pattern.matcher("a.txt").matches());
        assertFalse(pattern.matcher("b.txt").matches());
        assertFalse(pattern.matcher("c.txt").matches());
        assertTrue(pattern.matcher("d.txt").matches());
        assertFalse(pattern.matcher(".txt").matches());
    }
    
    @Test(expected = PatternSyntaxException.class)
    public void testEmptyCharClass() {
        GlobParser.globToPatternWithCaching("[]");
    }
    
    @Test(expected = PatternSyntaxException.class)
    public void testExplicitNameSeparatorInCharClass() {
        GlobParser.globToPatternWithCaching("[/]");
    }
    
    @Test(expected = PatternSyntaxException.class)
    public void testRangeCharAsFirstInCharClass() {
        GlobParser.globToPatternWithCaching("[-abc]");
    }
    
    @Test(expected = PatternSyntaxException.class)
    public void testRangeCharAsLastInCharClass() {
        GlobParser.globToPatternWithCaching("[abc-]");
    }
    
    @Test
    public void testBuiltInCharRangeInCharClass() {
        Pattern pattern = GlobParser.globToPatternWithCaching("[:digit:]");
        assertTrue(pattern.matcher("1").matches());
        assertFalse(pattern.matcher("a").matches());
    }
    
    @Test
    public void testBuiltInCharRangeInExcludedCharClass() {
        Pattern pattern = GlobParser.globToPatternWithCaching("[!:digit:]");
        assertTrue(pattern.matcher("a").matches());
        assertFalse(pattern.matcher("1").matches());
    }
}

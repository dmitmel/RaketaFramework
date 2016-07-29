package github.dmitmel.raketaframework.tests.util;

import github.dmitmel.raketaframework.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

public class ArraySplittingTest extends Assert {
    @Test
    public void testNonExistentDelimiterTesting() {
        byte[] content = {1, 2, 3};
        byte[] delimiter = {0};
        byte[][] actual = Arrays.splitArray(content, delimiter);
        byte[][] expected = {{1, 2, 3}};
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testNoContent() {
        byte[] content = {};
        byte[] delimiter = {0};
        byte[][] actual = Arrays.splitArray(content, delimiter);
        byte[][] expected = {{}};
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testOneElementDelimiter() {
        byte[] content = {1, 2, 3, 4, 5};
        byte[] delimiter = {3};
        byte[][] actual = Arrays.splitArray(content, delimiter);
        byte[][] expected = {{1, 2}, {4, 5}};
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testDelimiterAsFirstItem() {
        byte[] content = {1, 2, 3};
        byte[] delimiter = {1};
        byte[][] actual = Arrays.splitArray(content, delimiter);
        byte[][] expected = {{}, {2, 3}};
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testDelimiterAsLastItem() {
        byte[] content = {1, 2, 3};
        byte[] delimiter = {3};
        byte[][] actual = Arrays.splitArray(content, delimiter);
        byte[][] expected = {{1, 2}};
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testMultilineDelimiterDelimiterAsLastItem() {
        byte[] content = {1, 2, 3, 4};
        byte[] delimiter = {2, 3};
        byte[][] actual = Arrays.splitArray(content, delimiter);
        byte[][] expected = {{1}, {4}};
        assertArrayEquals(expected, actual);
    }
}

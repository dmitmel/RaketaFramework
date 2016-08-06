package org.willthisfly.raketaframework.testingapp;

import org.junit.Test;
import org.willthisfly.raketaframework.HTTPResponse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SerialRequestsTest extends ServerTestSuite {
    @Test
    public void testPlainTextRequest() {
        HTTPResponse response = doRequest("GET", "/hello");
        assertEquals("Hello, World!\n", response.bodyToString());
    }
    
    @Test
    public void testRequestWithUrlParameters() {
        HTTPResponse response1 = doRequest("GET", "/hello");
        assertEquals("Hello, World!\n", response1.bodyToString());
    
        HTTPResponse response2 = doRequest("GET", "/hello?who=Someone");
        assertEquals("Hello, Someone!\n", response2.bodyToString());
    
        HTTPResponse response3 = doRequest("GET", "/hello?who=Someone&how-many=3");
        assertEquals("Hello, Someone!\nHello, Someone!\nHello, Someone!\n", response3.bodyToString());
    
        HTTPResponse response4 = doRequest("GET", "/hello?greeting=Hi");
        assertEquals("Hi, World!\n", response4.bodyToString());
    }
    
    @Test
    public void testInternalServerError() {
        try {
            doRequest("GET", "/hello?how-many=abc");
            fail("Server did\'t return response code 500");
        } catch (RuntimeException e) {
            String message = e.getMessage();
            if (message == null || !message.contains("Server returned HTTP response code: 500"))
                fail("Server did\'t return response code 500");
        }
    }
    
    @Test
    public void testNotFound() {
        try {
            doRequest("GET", "/non-existent-page");
            fail("Server did\'t return response code 404");
        } catch (RuntimeException e) {
            Throwable cause = e.getCause();
            if (cause == null || !(cause instanceof java.io.FileNotFoundException))
                fail("Server did\'t return response code 404");
        }
    }
    
    @Test
    public void testCustomHTTPErrorGenerating() {
        try {
            doRequest("GET", "/file/non-existent.file");
            fail("Server did\'t return response code 404");
        } catch (RuntimeException e) {
            Throwable cause = e.getCause();
            if (cause == null || !(cause instanceof java.io.FileNotFoundException))
                fail("Server did\'t return response code 404");
        }
    }
    
    @Test
    public void testRegexpRoutes() {
        HTTPResponse response1 = doRequest("GET", "/regexp/say/Hello/to/World");
        assertEquals("Hello, World!", response1.bodyToString());
    }
    
    @Test
    public void testGlobRoutes() {
        HTTPResponse response1 = doRequest("GET", "/glob/say/Hello/to/World");
        assertEquals("Hello, World!", response1.bodyToString());
    }
    
    @Test
    public void testImageServing() throws IOException {
        BufferedImage imageFromFile = ImageIO.read(SerialRequestsTest.class.getResourceAsStream("/image.png"));
        
        HTTPResponse servedImageResponse = doRequest("GET", "/file/image.png");
        ByteArrayInputStream servedImageInputStream = new ByteArrayInputStream(servedImageResponse.body);
        BufferedImage servedImage = ImageIO.read(servedImageInputStream);
        
        assertTrue(areImagesEqual(imageFromFile, servedImage));
    }
    
    private boolean areImagesEqual(BufferedImage a, BufferedImage b) {
        if (a == null || b == null)
            return false;
        
        if (a.getWidth() != b.getWidth())
            return false;
        if (a.getHeight() != b.getHeight())
            return false;
        
        for (int x = 0; x < a.getWidth(); x++)
            for (int y = 0; y < a.getHeight(); y++)
                if (a.getRGB(x, y) != b.getRGB(x, y))
                    return false;
        
        return true;
    }
}

package github.dmitmel.raketaframework.tests.server;

import github.dmitmel.raketaframework.tests.TestApplication;
import github.dmitmel.raketaframework.web.HTTPResponse;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SerialRequestsTest extends ServerTestSuite {
    @Test
    public void testPlainTextRequest() {
        for (int i = 0; i < 5; i++) {
            HTTPResponse response = doRequest("GET", TestApplication.URL + "/hello-generator");
            assertEquals("Hello, World!\n", response.bodyToString());
        }
    }
    
    @Test
    public void testRequestWithUrlParameters() {
        HTTPResponse response1 = doRequest("GET", TestApplication.URL + "/hello-generator");
        assertEquals("Hello, World!\n", response1.bodyToString());
    
        HTTPResponse response2 = doRequest("GET", TestApplication.URL + "/hello-generator?who=Someone");
        assertEquals("Hello, Someone!\n", response2.bodyToString());
    
        HTTPResponse response3 = doRequest("GET", TestApplication.URL + "/hello-generator?who=Someone&how-many=3");
        assertEquals("Hello, Someone!\nHello, Someone!\nHello, Someone!\n", response3.bodyToString());
    
        HTTPResponse response4 = doRequest("GET", TestApplication.URL + "/hello-generator?greeting=Hi");
        assertEquals("Hi, World!\n", response4.bodyToString());
    }
    
    @Test
    public void testServerFailing() {
        try {
            doRequest("GET", TestApplication.URL + "/hello-generator?how-many=abc");
            fail("Server did\'t return response code 500");
        } catch (RuntimeException e) {
            String message = e.getMessage();
            if (message == null || !message.startsWith("Server returned HTTP response code: 500"))
                fail("Server did\'t return response code 500");
        }
    }
    
    @Test
    public void testImageServing() throws IOException {
        BufferedImage imageFromFile = ImageIO.read(SerialRequestsTest.class.getResourceAsStream("/image.png"));
        
        HTTPResponse servedImageResponse = doRequest("GET", TestApplication.URL + "/file/image.png");
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

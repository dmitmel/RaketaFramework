package github.dmitmel.raketaframework.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
//import java.io.OutputStream;

public class StringOutputStream extends OutputStream {
    private StringBuilder builder = new StringBuilder(0);

    public void write(int b) throws IOException {
        builder.append(b);
    }

    public String toString() {
        return builder.toString();
    }

    public static void main(String[] args) throws IOException {
        BufferedImage image = ImageIO.read(new BufferedInputStream(new FileInputStream("img.png")));
        ImageIO.write(image, "png", new BufferedOutputStream(new FileOutputStream("copy.png")));
    }
}

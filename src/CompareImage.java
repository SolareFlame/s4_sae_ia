import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class CompareImage {

    public double comparer(String originImgPath, String newImgPath) {
        File file1 = new File(originImgPath);
        File file2 = new File(newImgPath);

        if (file1.exists() && file2.exists()) {
            try {
                BufferedImage img1 = ImageIO.read(file1);
                BufferedImage img2 = ImageIO.read(file2);

                double score = 0.0;

                // Compare the images (Euclidean distance)
                for (int y = 0; y < img1.getHeight(); y++) {
                    for (int x = 0; x < img1.getWidth(); x++) {
                        int rgb1 = img1.getRGB(x, y);
                        int rgb2 = img2.getRGB(x, y);

                        //reduce to RGB
                        int r1 = (rgb1 >> 16) & 0xff;
                        int g1 = (rgb1 >> 8) & 0xff;
                        int b1 = rgb1 & 0xff;

                        int r2 = (rgb2 >> 16) & 0xff;
                        int g2 = (rgb2 >> 8) & 0xff;
                        int b2 = rgb2 & 0xff;

                        double distance = Math.sqrt(Math.pow(r1 - r2, 2) + Math.pow(g1 - g2, 2) + Math.pow(b1 - b2, 2));
                        score += distance;
                    }
                }

                return score;

            } catch (IOException e) {
                throw new RuntimeException("Erreur lors de la lecture des images", e);
            }
        }
        return -1;
    }
}

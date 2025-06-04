import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PaletteBrute {

    public static double[][] extrairePalette(String cheminImage) throws IOException {
        BufferedImage image = ImageIO.read(new File(cheminImage));

        int largeur = image.getWidth();
        int hauteur = image.getHeight();
        double[][] palette = new double[largeur * hauteur][3];

        int i =0;
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                int rgb = image.getRGB(x, y);
                double[] rgbPixel = new double[3];
                rgbPixel[0] = (rgb >> 16) & 0xFF; // Rouge
                rgbPixel[1] = (rgb >> 8) & 0xFF; // Vert
                rgbPixel[2] = (rgb & 0xFF); // Bleu
                palette[i++]=rgbPixel; // tab du RGB
            }
        }
        return palette;
    }
}

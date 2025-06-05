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

        int i = 0;
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


    public static int getAverageColor(BufferedImage image, double[][] coords) {
        long sumR = 0, sumG = 0, sumB = 0;

        for (double[] coord : coords) {
            int x = (int) coord[0];
            int y = (int) coord[1];
            int rgb = image.getRGB(x, y);

            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = rgb & 0xFF;

            sumR += r;
            sumG += g;
            sumB += b;
        }

        int n = coords.length;
        int avgR = (int)(sumR / n);
        int avgG = (int)(sumG / n);
        int avgB = (int)(sumB / n);

        return (avgR << 16) | (avgG << 8) | avgB;
    }


    public static int[] genColorVariations(int baseColor, int n) {
        int[] variations = new int[n];
        for (int i = 0; i < n; i++) {
            int variation = (baseColor + (i * 0x111111)) & 0xFFFFFF;
            variations[i] = variation;
        }
        return variations;
    }

}

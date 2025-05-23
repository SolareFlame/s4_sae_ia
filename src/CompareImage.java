import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class CompareImage {

    // Classe représentant une couleur RGB avec une égalité basée sur les valeurs
    static class RGB {
        int r, g, b;

        public RGB(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof RGB)) return false;
            RGB rgb = (RGB) o;
            return r == rgb.r && g == rgb.g && b == rgb.b;
        }

        @Override
        public int hashCode() {
            return 31 * r + 17 * g + b;
        }
    }

    public Map<RGB, Integer> histogram(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();

        Map<RGB, Integer> histogram = new HashMap<>();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = img.getRGB(i, j);

                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;

                RGB key = new RGB(r, g, b);
                histogram.put(key, histogram.getOrDefault(key, 0) + 1);
            }
        }
        return histogram;
    }

    public double comparer(String path1, String path2) {
        File file1 = new File(path1);
        File file2 = new File(path2);

        if (file1.exists() && file2.exists()) {
            try {
                BufferedImage img1 = ImageIO.read(file1);
                BufferedImage img2 = ImageIO.read(file2);

                Map<RGB, Integer> histogram1 = histogram(img1);
                Map<RGB, Integer> histogram2 = histogram(img2);

                int size1 = histogram1.values().stream().mapToInt(Integer::intValue).sum();
                int size2 = histogram2.values().stream().mapToInt(Integer::intValue).sum();

                double distance = 0;

                // Normalisation en pourcentage
                for (Map.Entry<RGB, Integer> entry : histogram1.entrySet()) {
                    RGB key = entry.getKey();
                    double freq1 = entry.getValue() / (double) size1;
                    double freq2 = histogram2.getOrDefault(key, 0) / (double) size2;

                    distance += Math.abs(freq1 - freq2);
                }

                for (Map.Entry<RGB, Integer> entry : histogram2.entrySet()) {
                    RGB key = entry.getKey();
                    if (!histogram1.containsKey(key)) {
                        double freq2 = entry.getValue() / (double) size2;
                        distance += freq2;
                    }
                }

                return distance;

            } catch (IOException e) {
                throw new RuntimeException("Erreur lors de la lecture des images", e);
            }
        }
        return -1;
    }
}

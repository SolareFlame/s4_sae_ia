import java.awt.*;

public class Palette {

    Color[] colors;
    NormeCouleurs norme;

    public Palette(Color[] colors, NormeCouleurs norme) {
        this.colors = colors;
        this.norme = norme;
    }

    public Color getPlusProche(Color color) {
        int[] c1 = OutilCouleur.getTabColor(color.getRGB());

        double min_d = Double.MAX_VALUE;
        Color min_c = null;

        for (Color current : colors) {
            int[] c2 = OutilCouleur.getTabColor(current.getRGB());
            double d1_2 = Math.pow(c1[0] - c2[0], 2) + Math.pow(c1[1] - c2[1], 2) + Math.pow(c1[2] - c2[2], 2);

            if (d1_2 < min_d) {
                min_d = d1_2;
                min_c = current;
            }
        }

        return min_c;
    }

    public Color getPlusProcheNorme(Color color) {
        double min_d = Double.MAX_VALUE;
        Color min_c = null;

        for (Color current : colors) {
            double d1_2 = norme.distanceCouleur(color, current);

            if (d1_2 < min_d) {
                min_d = d1_2;
                min_c = current;
            }
        }

        return min_c;
    }
}

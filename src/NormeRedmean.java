import java.awt.*;

public class NormeRedmean implements NormeCouleurs {
    @Override
    public double distanceCouleur(Color c1c, Color c2c) {
        int[] c1 = OutilCouleur.getTabColor(c1c.getRGB());
        int[] c2 = OutilCouleur.getTabColor(c2c.getRGB());

        double r = (double) (c1[0] + c2[0]) / 2;

        double dr = c1[0] - c2[0];
        double dg = c1[1] - c2[1];
        double db = c1[2] - c2[2];

        return Math.sqrt((2 + r/256) * Math.pow(dr, 2) + 4 * Math.pow(dg, 2) + (2 + (255 - r)/256) * Math.pow(db, 2));
    }
}

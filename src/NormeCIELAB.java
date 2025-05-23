import java.awt.*;

public class NormeCIELAB implements NormeCouleurs {
    @Override
    public double distanceCouleur(Color c1c, Color c2c) {
        int[] c1 = OutilCouleur.getTabColor(c1c.getRGB());
        int[] c2 = OutilCouleur.getTabColor(c2c.getRGB());

        int[] lab1 = RGBtoLAB.transformLAB(c1[0], c1[1], c1[2]);
        int[] lab2 = RGBtoLAB.transformLAB(c2[0], c2[1], c2[2]);


        double C1 = Math.sqrt(Math.pow(lab1[1], 2) + Math.pow(lab1[2], 2));
        double C2 = Math.sqrt(Math.pow(lab2[1], 2) + Math.pow(lab2[2], 2));

        double L = lab1[0] - lab2[0];
        double C = C1 - C2;
        double H = Math.sqrt(Math.pow(lab1[1] - lab2[1], 2) + Math.pow(lab1[2] - lab2[2], 2) - Math.pow(C, 2));

        double Sc = 1 + 0.045 * C1;
        double Sh = 1 + 0.015 * C1;

        return Math.sqrt(Math.pow(L, 2) + Math.pow(C / Sc, 2) + Math.pow(H / Sh, 2));
    }
}

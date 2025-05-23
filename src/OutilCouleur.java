public class OutilCouleur {

    static int[] getTabColor(int c) {
        int[] tab = new int[3];
        tab[0] = (c >> 16) & 0xFF; //R
        tab[1] = (c >> 8) & 0xFF; //G
        tab[2] = c & 0xFF; //B
        return tab;
    }

    static int getCouleur(int r, int g, int b) {
        return (r << 16) | (g << 8) | b;

        //G + G * 256 * G + 256²
    }
}

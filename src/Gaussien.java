import java.awt.image.BufferedImage;

public class Gaussien {

    public static BufferedImage appliquerFlouGaussien(BufferedImage source, int rayon, double sigma) {
        int largeur = source.getWidth();
        int hauteur = source.getHeight();

        // Génération du noyau avec la formule gaussienne
        float[] noyau = genererNoyauGaussien(rayon, sigma);
        int taille = 2 * rayon + 1;

        BufferedImage resultat = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_ARGB);

        // Application du "filtre de convolution"
        for (int y = rayon; y < hauteur - rayon; y++) {
            for (int x = rayon; x < largeur - rayon; x++) {
                float rouge = 0, vert = 0, bleu = 0, alpha = 0;

                // Parcours du noyau
                for (int ny = 0; ny < taille; ny++) {
                    for (int nx = 0; nx < taille; nx++) {
                        int px = x + nx - rayon;
                        int py = y + ny - rayon;

                        int rgb = source.getRGB(px, py);
                        float poids = noyau[ny * taille + nx];

                        alpha += ((rgb >> 24) & 0xFF) * poids;
                        rouge += ((rgb >> 16) & 0xFF) * poids;
                        vert  += ((rgb >> 8)  & 0xFF) * poids;
                        bleu  += (rgb & 0xFF) * poids;
                    }
                }

                int rgbFlou = ((int)alpha << 24) | ((int)rouge << 16) |
                        ((int)vert << 8) | (int)bleu;
                resultat.setRGB(x, y, rgbFlou);
            }
        }
        return resultat;
    }


    public static float[] genererNoyauGaussien(int rayon, double sigma) {
        int taille = 2 * rayon + 1;
        float[] noyau = new float[taille * taille];
        double somme = 0.0;

        // Application de la formule G(x,y) = (1/2πσ²)e^(-(x²+y²)/2σ²)
        for (int y = -rayon; y <= rayon; y++) {
            for (int x = -rayon; x <= rayon; x++) {
                // Calcul de la distance au carré : x² + y²
                double distanceCarree = x * x + y * y;

                // Application de la formule
                double valeur = (1.0 / (2.0 * Math.PI * sigma * sigma)) *
                        Math.exp(-distanceCarree / (2.0 * sigma * sigma));

                int index = (y + rayon) * taille + (x + rayon);
                noyau[index] = (float) valeur;
                somme += valeur;
            }
        }

        // Normalisation pour que la somme soit égale à 1
        for (int i = 0; i < noyau.length; i++) {
            noyau[i] /= (float) somme;
        }

        return noyau;
    }
}
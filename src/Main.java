import algo.Clustering;
import algo.DbScan;
import algo.HAC;
import algo.KMeans;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException  {
        String cheminImageFloue = "./res/Image floue.png";

        final String planete1 = "./res/Planete 1.jpg";
        final String planete2 = "./res/Planete 2.jpg";
        final String planete3 = "./res/Planete 3.jpg";
        final String planete4 = "./res/Planete 4.jpg";
        final String planete5 = "./res/Planete 5.jpg";

        final String fichier_choisie = planete1;

        File fichierSource = new File(fichier_choisie);

        // Vérifier que le fichier source existe
        BufferedImage image = ImageIO.read(fichierSource);
        BufferedImage imageFloue = Gaussien.appliquerFlouGaussien(image, 5, 1.0);

        boolean success = ImageIO.write(imageFloue, "PNG", new File(cheminImageFloue));

        System.out.println(success ? "Image floue générée avec succès : " + fichier_choisie : "Échec de l'écriture de l'image");

        // Exemple de données factices
        double[][] data = new double[100][3];
        for (int i = 0; i < 100; i++) {
            data[i][0] = Math.random();
            data[i][1] = Math.random();
            data[i][2] = Math.random();
        }

        // Exemple de données factices
        double[][] data2 = new double[100][3];
        for (int i = 0; i < 70; i++) { // Bleu
            data2[i][0] = 0; // Rouge
            data2[i][1] = 0; // Vert
            data2[i][2] = 255; // Bleu
        }
        for (int i = 70; i < 85; i++) { // Vert
            data2[i][0] = 0; // Rouge
            data2[i][1] = 255; // Vert
            data2[i][2] = 0; // Bleu
        }
        for (int i = 85; i < 100; i++) { // Rouge
            data2[i][0] = 255; // Rouge
            data2[i][1] = 0; // Vert
            data2[i][2] = 0; // Bleu
        }

        // KMeans
        Clustering km = new KMeans(3, 100);
        int[] labelsKm = km.calculer(data2);
        System.out.println("Labels KMeans : " + java.util.Arrays.toString(labelsKm));

        // DBSCAN
        /*
        Clustering db = new DbScan(0.3, 5);
        int[] labelsDb = db.calculer(data2);
        System.out.println("Labels DBSCAN : " + java.util.Arrays.toString(labelsDb));
        */

        // HAC
        Clustering hac = new HAC(HAC.Linkage.AVERAGE, 15);
        int[] labelsHac = hac.calculer(data2);
        System.out.println("Labels HAC : " + java.util.Arrays.toString(labelsHac));
    }
}

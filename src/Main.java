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

        final String nom_fichier = "./res/Planete 1.jpg";
        File fichierSource = new File(nom_fichier);

        // Vérifier que le fichier source existe
        BufferedImage image = ImageIO.read(fichierSource);
        BufferedImage imageFloue = Gaussien.appliquerFlouGaussien(image, 5, 1.0);

        boolean success = ImageIO.write(imageFloue, "PNG", new File(nom_fichier));

        System.out.println(success ? "Image floue générée avec succès : " + nom_fichier : "Échec de l'écriture de l'image");

        // Exemple de données factices
        double[][] X = new double[100][2];
        for (int i = 0; i < 100; i++) {
            X[i][0] = Math.random();
            X[i][1] = Math.random();
        }

        // KMeans
        Clustering km = new KMeans(3, 100);
        int[] labelsKm = km.calculer(X);
        System.out.println("Labels KMeans : " + java.util.Arrays.toString(labelsKm));

        // DBSCAN
/*        Clustering db = new DbScan(0.3, 5);
        int[] labelsDb = db.calculer(X);
        System.out.println("Labels DBSCAN : " + java.util.Arrays.toString(labelsDb));*/

        // HAC
        Clustering hac = new HAC(HAC.Linkage.AVERAGE, 3);
        int[] labelsHac = hac.calculer(X);
        System.out.println("Labels HAC : " + java.util.Arrays.toString(labelsHac));
    }
}

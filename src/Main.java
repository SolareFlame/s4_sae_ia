import algo.Clustering;
import algo.DbScan;
import algo.HAC;
import algo.KMeans;

public class Main {
    public static void main(String[] args) {
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
        Clustering db = new DbScan(0.3, 5);
        int[] labelsDb = db.calculer(X);
        System.out.println("Labels DBSCAN : " + java.util.Arrays.toString(labelsDb));

        // HAC
        Clustering hac = new HAC(HAC.Linkage.AVERAGE, 3);
        int[] labelsHac = hac.calculer(X);
        System.out.println("Labels HAC : " + java.util.Arrays.toString(labelsHac));
    }
}
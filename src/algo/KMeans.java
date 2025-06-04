package algo;
import java.util.*;
/*
Entrées : ng ≥ 0 nombre de groupes
Entrées : D = {di }i données
Résultat : Centroïdes mis à jour
 */

public class KMeans implements Clustering {

    private int k;
    private int max_iterations;

    public KMeans(int k, int max_iterations) {
        this.k = k; // NG
        this.max_iterations = max_iterations;
    }

    @Override
    public int[] calculer(double[][] data) {
        int nb_o = data.length; // Nombre d'objets
        int nb_c = data[0].length; // Nombre de caractéristiques

        // Initialisation des centroïdes
        // 1 pour i ∈ [0, ng ] faire
        // 2 ci ← random(D)
        double[][] centroids = new double[k][nb_c];
        Random random = new Random();
        for (int i = 0; i < k; i++) {
            int rdm_i = random.nextInt(nb_o);
            centroids[i] = Arrays.copyOf(data[rdm_i], nb_c);
        }

        int[] assignments = new int[nb_o];
        boolean centroids_changed = true;
        int nb_iter = 0;

        while (centroids_changed && nb_iter < max_iterations) {
            nb_iter++;
            centroids_changed = false;

            // Initialisation Groupes
            // 4 pour i ∈ [0, ng ] faire
            // 5 Gi ← ∅
            List<List<double[]>> groups = new ArrayList<>();
            for (int i = 0; i < k; i++) {
                groups.add(new ArrayList<>());
            }

            // Construction des Groupes
            // 6 pour d ∈ D faire
            // 7 k ← indiceCentroidePlusProche(d, {ci }i )
            // 8 Gk ← Gk ∪ d
            for (int i = 0; i < nb_o; i++) {
                double[] d = data[i];
                int closest = 0;
                double min_dist = Double.MAX_VALUE;

                for (int j = 0; j < k; j++) {
                    double dist = distance(d, centroids[j]);
                    if (dist < min_dist) {
                        min_dist = dist;
                        closest = j;
                    }
                }

                groups.get(closest).add(d);
                assignments[i] = closest;
            }

            // Mise à jour des centroïdes
            // 9 pour i ∈ [0, ng ] faire
            // 10 ci ← barycentre(Gi )
            for (int i = 0; i < k; i++) {
                List<double[]> group = groups.get(i);
                if (group.isEmpty()) continue;

                double[] new_centroid = new double[nb_c];
                for (double[] point : group) {
                    for (int j = 0; j < nb_c; j++) {
                        new_centroid[j] += point[j];
                    }
                }

                for (int j = 0; j < nb_c; j++) {
                    new_centroid[j] /= group.size();
                }

                if (!Arrays.equals(centroids[i], new_centroid)) {
                    centroids_changed = true;
                    centroids[i] = new_centroid;
                }
            }
        }

        return assignments;
    }

    private double distance(double[] a, double[] b) {
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += Math.pow(a[i] - b[i], 2);
        }
        return Math.sqrt(sum);
    }
}

package algo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Pour comprendre le fonctionnement de l'algorithme, voir la vidéo : https://www.youtube.com/watch?v=8QCBl-xdeZI
 */
public class HAC implements Clustering {

    public enum Linkage {
        SINGLE,
        COMPLETE,
        AVERAGE,
        CENTROID
    }

    private final Linkage linkage;
    private final int nClusters;

    /**
     * @param linkage méthode de linkage (SINGLE, COMPLETE, AVERAGE, CENTROID)
     * @param nClusters nombre de clusters final
     */
    public HAC(Linkage linkage, int nClusters) {
        this.linkage = linkage;
        this.nClusters = nClusters;
        if (nClusters < 1) {
            throw new IllegalArgumentException("nClusters doit être >= 1");
        }
    }

    @Override
    public int[] calculer(double[][] data) {
        int n = data.length;

        // Initialiser chaque point dans son propre cluster
        List<List<Integer>> clusters = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            clusters.add(new ArrayList<>(Arrays.asList(i)));
        }

        // Calcul initial de la matrice de distances entre échantillons (tableau de la vidéo)
        double[][] dist = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double d = distanceEuclidienne(data[i], data[j]);
                dist[i][j] = d;
                dist[j][i] = d;
            }
        }

        // Fusion itérative jusqu'à obtenir nClusters
        while (clusters.size() > nClusters) {
            // Trouver la paire de clusters à distance minimale selon le linkage
            int ci = -1, cj = -1;
            double bestDist = Double.MAX_VALUE;
            for (int a = 0; a < clusters.size(); a++) {
                for (int b = a + 1; b < clusters.size(); b++) {
                    double dAB = distanceClusters(clusters.get(a), clusters.get(b), data, dist);
                    if (dAB < bestDist) {
                        bestDist = dAB;
                        ci = a; cj = b;
                    }
                }
            }
            // Fusionner cluster ci et cj
            clusters.get(ci).addAll(clusters.get(cj));
            clusters.remove(cj);
        }

        // Générer le vecteur de labels
        int[] labels = new int[n];
        for (int label = 0; label < clusters.size(); label++) {
            for (int idx : clusters.get(label)) {
                labels[idx] = label;
            }
        }
        return labels;
    }

    private double distanceEuclidienne(double[] a, double[] b) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            double diff = a[i] - b[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }

    private double distanceClusters(List<Integer> c1, List<Integer> c2,
                                    double[][] data, double[][] distMatrix) {
        return switch (linkage) {
            case SINGLE -> singleLinkage(c1, c2, distMatrix);
            case COMPLETE -> completeLinkage(c1, c2, distMatrix);
            case AVERAGE -> averageLinkage(c1, c2, distMatrix);
            case CENTROID -> centroidLinkage(c1, c2, data);
            default -> throw new IllegalStateException("Linkage non reconnu");
        };
    }

    /**
     * @param c1 1er cluster
     * @param c2 2ème cluster
     * @param distMatrix matrice de distances
     * @return la distance minimum entre ces deux clusters
     */
    private double singleLinkage(List<Integer> c1, List<Integer> c2, double[][] distMatrix) {
        double min = Double.MAX_VALUE;
        for (int i : c1) {
            for (int j : c2) {
                min = Math.min(min, distMatrix[i][j]);
            }
        }
        return min;
    }

    /**
     * @param c1 1er cluster
     * @param c2 2ème cluster
     * @param distMatrix matrice de distances
     * @return la distance maximum entre ces deux clusters
     */
    private double completeLinkage(List<Integer> c1, List<Integer> c2, double[][] distMatrix) {
        double max = Double.MIN_VALUE;
        for (int i : c1) {
            for (int j : c2) {
                max = Math.max(max, distMatrix[i][j]);
            }
        }
        return max;
    }

    /**
     * @param c1 1er cluster
     * @param c2 2ème cluster
     * @param distMatrix matrice de distances
     * @return la distance moyenne entre ces deux clusters (distance avec tt ces points)
     */
    private double averageLinkage(List<Integer> c1, List<Integer> c2, double[][] distMatrix) {
        double sum = 0;
        int count = 0;
        for (int i : c1) {
            for (int j : c2) {
                sum += distMatrix[i][j];
                count++;
            }
        }
        return sum / count;
    }

    /* pas encore comprit cette partie */

    private double centroidLinkage(List<Integer> c1, List<Integer> c2, double[][] data) {
        double[] mu1 = calculerCentre(c1, data);
        double[] mu2 = calculerCentre(c2, data);
        return distanceEuclidienne(mu1, mu2);
    }

    private double[] calculerCentre(List<Integer> cluster, double[][] data) {
        int dim = data[0].length;
        double[] mu = new double[dim];
        for (int idx : cluster) {
            for (int d = 0; d < dim; d++) {
                mu[d] += data[idx][d];
            }
        }
        for (int d = 0; d < dim; d++) {
            mu[d] /= cluster.size();
        }
        return mu;
    }
}

package algo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implémentation du clustering hiérarchique agglomératif (HAC) qui
 * gère automatiquement les doublons dans data (ex. plusieurs pixels
 * de même couleur). La méthode calculer(double[][] data) déduplique
 * les lignes identiques, fait le clustering sur les vecteurs uniques,
 * puis réaffecte un label à chaque objet d'origine.
 */
public class EnhancedHAC implements Clustering {

    public enum Linkage {
        SINGLE,
        COMPLETE,
        AVERAGE,
    }

    private final Linkage linkage;
    private final int nClusters;

    /**
     * @param linkage   méthode de linkage à utiliser ({@code SINGLE}, {@code COMPLETE}, {@code AVERAGE})
     * @param nClusters nombre de clusters finaux (doit être >= 1)
     */
    public EnhancedHAC(Linkage linkage, int nClusters) {
        if (nClusters < 1) {
            throw new IllegalArgumentException("nClusters doit être >= 1");
        }
        this.linkage = linkage;
        this.nClusters = nClusters;
    }

    /**
     * Effectue un clustering HAC sur les données fournies. Si plusieurs
     * lignes de data sont identiques, elles seront considérées comme un
     * seul point lors du clustering, mais dans le résultat final, chaque
     * ligne d'origine recevra le même label.
     *
     * @param data tableau de taille Nobjet x Ncarac représentant les objets à classer
     * @return tableau de taille Nobjet contenant le numéro de cluster attribué à chaque objet
     */
    @Override
    public int[] calculer(double[][] data) {
        int n = data.length;
        if (n == 0) {
            return new int[0];
        }

        // 1) Identifier les vecteurs uniques et créer :
        //    uniqueList : liste des vecteurs distincts
        //    mapping    : pour chaque index d'origine i, l'index j dans uniqueList
        Map<String, Integer> uniqueMap = new HashMap<>();
        List<double[]> uniqueList = new ArrayList<>();
        int[] originToUnique = new int[n];

        for (int i = 0; i < n; i++) {
            // On convertit la ligne data[i] en une clé chaîne pour la Map
            String key = Arrays.toString(data[i]);
            if (!uniqueMap.containsKey(key)) {
                uniqueList.add(data[i]);
                uniqueMap.put(key, uniqueList.size() - 1);
            }
            originToUnique[i] = uniqueMap.get(key);
        }

        int m = uniqueList.size(); // nombre de vecteurs uniques

        // 2) Construire le tableau uniqueData[m][Ncarac]
        int dim = data[0].length;
        double[][] uniqueData = new double[m][dim];
        for (int j = 0; j < m; j++) {
            uniqueData[j] = uniqueList.get(j);
        }

        // 3) Construire la matrice de distances m×m
        double[][] dist = new double[m][m];
        for (int i = 0; i < m; i++) {
            dist[i][i] = 0.0;
            for (int j = i + 1; j < m; j++) {
                double d = distanceEuclidienne(uniqueData[i], uniqueData[j]);
                dist[i][j] = d;
                dist[j][i] = d;
            }
        }

        // 4) Créer m clusters singleton [[0], [1], ..., [m-1]]
        List<List<Integer>> clusters = new ArrayList<>(m);
        for (int i = 0; i < m; i++) {
            clusters.add(new ArrayList<>(List.of(i)));
        }

        // 5) Fusion itérative jusqu'à nClusters
        while (clusters.size() > nClusters) {
            double bestDist = Double.MAX_VALUE;
            int ci = -1, cj = -1;

            // Parcourir toutes les paires de clusters
            for (int a = 0; a < clusters.size(); a++) {
                for (int b = a + 1; b < clusters.size(); b++) {
                    double dAB = distanceClusters(clusters.get(a), clusters.get(b), dist);
                    if (dAB < bestDist) {
                        bestDist = dAB;
                        ci = a;
                        cj = b;
                    }
                }
            }

            // Fusionner cluster ci et cj
            clusters.get(ci).addAll(clusters.get(cj));
            clusters.remove(cj);
        }

        // 6) Construire les labels pour chaque vecteur unique
        int[] labelsUnique = new int[m];
        for (int label = 0; label < clusters.size(); label++) {
            for (int idx : clusters.get(label)) {
                labelsUnique[idx] = label;
            }
        }

        // 7) Réaffecter un label à chaque objet d'origine
        int[] resultLabels = new int[n];
        for (int i = 0; i < n; i++) {
            int uniqueIdx = originToUnique[i];
            resultLabels[i] = labelsUnique[uniqueIdx];
        }

        return resultLabels;
    }

    /** Distance euclidienne entre deux vecteurs (de même longueur). */
    private double distanceEuclidienne(double[] a, double[] b) {
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            double diff = a[i] - b[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }

    /**
     * Calcule la distance entre deux clusters c1 et c2 selon la méthode linkage choisie.
     *
     * @param c1         liste d'indices du 1er cluster
     * @param c2         liste d'indices du 2nd cluster
     * @param distMatrix matrice pré-calculée dist[i][j] entre vecteurs i et j
     * @return distance entre c1 et c2 (single, complete ou average)
     */
    private double distanceClusters(List<Integer> c1, List<Integer> c2, double[][] distMatrix) {
        return switch (linkage) {
            case SINGLE -> singleLinkage(c1, c2, distMatrix);
            case COMPLETE -> completeLinkage(c1, c2, distMatrix);
            case AVERAGE -> averageLinkage(c1, c2, distMatrix);
            default -> throw new IllegalStateException("Linkage non reconnu");
        };
    }

    /** SINGLE linkage = min_{i∈c1, j∈c2} distMatrix[i][j] */
    private double singleLinkage(List<Integer> c1, List<Integer> c2, double[][] distMatrix) {
        double min = Double.MAX_VALUE;
        for (int i : c1) {
            for (int j : c2) {
                double d = distMatrix[i][j];
                if (d < min) {
                    min = d;
                }
            }
        }
        return min;
    }

    /** COMPLETE linkage = max_{i∈c1, j∈c2} distMatrix[i][j] */
    private double completeLinkage(List<Integer> c1, List<Integer> c2, double[][] distMatrix) {
        double max = Double.MIN_VALUE;
        for (int i : c1) {
            for (int j : c2) {
                double d = distMatrix[i][j];
                if (d > max) {
                    max = d;
                }
            }
        }
        return max;
    }

    /**
     * AVERAGE linkage = (1 / (|c1| * |c2|)) * Σ_{i∈c1, j∈c2} distMatrix[i][j]
     */
    private double averageLinkage(List<Integer> c1, List<Integer> c2, double[][] distMatrix) {
        double sum = 0.0;
        int count = 0;
        for (int i : c1) {
            for (int j : c2) {
                sum += distMatrix[i][j];
                count++;
            }
        }
        return (count > 0) ? (sum / count) : Double.MAX_VALUE;
    }
}

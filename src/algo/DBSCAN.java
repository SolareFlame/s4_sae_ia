package algo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DBSCAN implements Clustering {
    private final double eps;
    private final int minPts;

    /**
     * @param eps rayon de voisinage
     * @param minPts nombre minimal de points pour former un cluster dense
     */
    public DBSCAN(double eps, int minPts) {
        this.eps = eps;
        this.minPts = minPts;
    }

    @Override
    public int[] calculer(double[][] data) {
        int n = data.length;
        int[] labels = new int[n]; // 0 = non visité, -1 = bruit
        boolean[] visited = new boolean[n];
        int clusterId = 0;

        for (int i = 0; i < n; i++) {
            if (visited[i]) continue;

            visited[i] = true;
            List<Integer> neighbors = regionQuery(data, i);

            if (neighbors.size() < minPts) {
                labels[i] = -1; // Marquer comme bruit
            } else {
                clusterId++;
                expandCluster(data, i, neighbors, labels, visited, clusterId);
            }
        }
        return labels;
    }

    private void expandCluster(double[][] data, int index, List<Integer> neighbors,
                               int[] labels, boolean[] visited, int clusterId) {
        labels[index] = clusterId;

        LinkedList<Integer> queue = new LinkedList<>(neighbors);

        while (!queue.isEmpty()) {
            int current = queue.poll();

            if (!visited[current]) {
                visited[current] = true;
                List<Integer> currentNeighbors = regionQuery(data, current);

                if (currentNeighbors.size() >= minPts) {
                    queue.addAll(currentNeighbors);
                }
            }

            if (labels[current] == -1 || labels[current] == 0) {
                labels[current] = clusterId;
            }
        }
    }

    private List<Integer> regionQuery(double[][] data, int index) {
        List<Integer> neighbors = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            if (i != index && distance(data[index], data[i]) <= eps) {
                neighbors.add(i);
            }
        }
        return neighbors;
    }

    private double distance(double[] a, double[] b) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += Math.pow(a[i] - b[i], 2);
        }
        return Math.sqrt(sum);
    }
}

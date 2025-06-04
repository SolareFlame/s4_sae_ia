package algo;

public class DbScan implements Clustering {

    public DbScan(double epsilon, int minPts) {
        if (epsilon <= 0) {
            throw new IllegalArgumentException("Epsilon doit être > 0");
        }
        if (minPts < 1) {
            throw new IllegalArgumentException("minPts doit être >= 1");
        }
    }

    @Override
    public int[] calculer(double[][] data) {
        //TODO
        return null;
    }
}

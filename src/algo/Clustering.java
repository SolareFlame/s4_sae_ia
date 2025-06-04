package algo;

public interface Clustering {

    /**
     * Effectue un clustering sur les données fournies.
     *
     * @param data tableau de taille Nobjet x Ncarac représentant les objets à classer
     * @return tableau de taille Nobjet contenant le numéro de cluster attribué à chaque objet
     */
    int[] calculer(double[][] data);
}

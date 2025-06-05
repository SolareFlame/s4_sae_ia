import algo.Clustering;
import algo.KMeans;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

static int[] MAPCOLORS = {
        0x8B0000,  // DarkRed
        0x006400,  // DarkGreen
        0x00008B,  // DarkBlue
        0x8B008B,  // DarkMagenta
        0x808000,  // Olive
        0xFF4500,  // OrangeRed
        0x2E8B57,  // SeaGreen
        0x4682B4,  // SteelBlue
};



public static void main(String[] args) throws IOException {
    String cheminImage = "./res/Planete 1.jpg";
    String cheminImageFloue = "./res/Image_floue.png";
    int nb_cluster_biomes = 5;
    int nb_cluster_regions = 5;

    // FLOU GAUSSIEN
    File fichierSource = new File(cheminImage);
    BufferedImage image = ImageIO.read(fichierSource);
    BufferedImage imageFloue = Gaussien.appliquerFlouGaussien(image, 5, 1.0);

    int largeur = imageFloue.getWidth();
    int hauteur = imageFloue.getHeight();


    boolean success = ImageIO.write(imageFloue, "PNG", new File(cheminImageFloue));
    System.out.println(success ? "Image floue générée avec succès : " + cheminImageFloue : "Échec de l'écriture de l'image");

    // EXTRACTION DE LA PALETTE
    double[][] palette;
    palette = PaletteBrute.extrairePalette(cheminImageFloue);

    // EXTRACTION DES BIOMES
    Clustering km = new KMeans(nb_cluster_biomes, 100);
    int[] labels_km = km.calculer(palette);
    System.out.println("DATA SIZE: " + palette.length);

    List<double[]>[] biomeLists = new ArrayList[nb_cluster_biomes];
    for (int i = 0; i < nb_cluster_biomes; i++) {
        biomeLists[i] = new ArrayList<>();
    }

    for (int i = 0; i < labels_km.length; i++) {
        if (i % 5000 == 0) {
            System.out.println("Pourcentage de progression: " + (i * 100 / labels_km.length) + "%");
        }

        int biome_index = labels_km[i];
        double[] coords = new double[] { i % largeur, i / largeur };
        biomeLists[biome_index].add(coords);
    }

    double[][][] biomes = new double[nb_cluster_biomes][][];
    for (int i = 0; i < nb_cluster_biomes; i++) {
        biomes[i] = biomeLists[i].toArray(new double[0][0]);
    }

    System.out.println("FINI L'EXTRACTION DES BIOMES");


    // GENERATION DES COULEURS DE REGIONS
    // n-1 varations des couleurs des biomes pour les n régions (n = nb_cluster_regions)

    BufferedImage rendu_final = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_RGB);

    for (int i = 0; i < biomes.length; i++) {
        System.out.println("BIOME " + i + " : " + biomes[i].length + " pixels");

        if (biomes[i] == null || biomes[i].length == 0) continue;

        Clustering clustering = new KMeans(nb_cluster_regions, 1000); //new HAC(HAC.Linkage.AVERAGE, nb_cluster_regions);
        int[] regions = clustering.calculer(biomes[i]);

        int biome_color = PaletteBrute.getAverageColor(imageFloue, biomes[i]);
        int[] regions_colors = PaletteBrute.genColorVariations(biome_color, nb_cluster_regions);

        for (int j = 0; j < biomes[i].length; j++) {
            int x = (int) biomes[i][j][0];
            int y = (int) biomes[i][j][1];

            int region_index = regions[j];

            rendu_final.setRGB(x, y, regions_colors[region_index]);
        }
    }

    File output = new File("./res/regions_output.png");
    ImageIO.write(rendu_final, "PNG", output);
    System.out.println("Image des régions créée : " + output.getPath());
}





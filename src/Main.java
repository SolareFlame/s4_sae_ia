import algo.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


class Main {
    public static void main(String[] args) throws IOException {
        String cheminImage = "./res/Planete 1.jpg";
        String cheminImageFloue = "./res/Image_floue.png";
        int nb_cluster_biomes = 15;
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
        Clustering km = new KMeans(nb_cluster_biomes, 1000);
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
            double[] coords = new double[]{i % largeur, (double) i / largeur};
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
            BufferedImage rendu_region = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_RGB);

            System.out.println("BIOME " + i + " : " + biomes[i].length + " pixels");

            if (biomes[i] == null || biomes[i].length == 0) continue;

            Clustering clustering = new KMeans(nb_cluster_biomes, 1000);
            int[] regions = clustering.calculer(biomes[i]);

            int biome_color = PaletteBrute.getAverageColor(imageFloue, biomes[i]);
            int[] regions_colors = PaletteBrute.genColorVariations(biome_color, nb_cluster_regions);

            for (int j = 0; j < biomes[i].length; j++) {
                int x = (int) biomes[i][j][0];
                int y = (int) biomes[i][j][1];

                int region_index = regions[j];

                if (region_index < 0 || region_index >= regions_colors.length) {
                    System.out.println("Index de région invalide : " + region_index + " pour le pixel (" + x + ", " + y + ")");
                    continue;
                }

                rendu_final.setRGB(x, y, regions_colors[region_index]);
                rendu_region.setRGB(x, y, regions_colors[region_index]);
            }

            File output = new File("./res/regions_output_"+i+".png");
            ImageIO.write(rendu_region, "PNG", output);
            System.out.println("Image des région créée : " + output.getPath());
        }

        File output = new File("./res/regions_output.png");
        ImageIO.write(rendu_final, "PNG", output);
        System.out.println("Image des régions créée : " + output.getPath());
    }
}


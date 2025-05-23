import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.nio.file.Files.write;

public class CopieImage {

    /**
     * Copie classique (1.1)
     * @param path chemin de l'image à copier
     */
    public void copier(String path) {
        File file = new File(path);
        if (file.exists()) {
            try {
                BufferedImage img = ImageIO.read(file);

                File output = new File(file.getParent(), "copie_" + file.getName());
                ImageIO.write(img, "png", output);

                System.out.println("Image copiée : " + output.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Copie pixel par pixel (1.2)
     * @param path chemin de l'image à copier
     */
    public void copierPx(String path) {
        File file = new File(path);
        if (file.exists()) {
            try {
                BufferedImage img = ImageIO.read(file);

                File output = new File(file.getParent(), "copiepx_" + file.getName());
                BufferedImage output_img = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

                for (int i = 0; i < img.getWidth(); i++) {
                    for (int j = 0; j < img.getHeight(); j++) {
                        int pixel = img.getRGB(i, j);
                        output_img.setRGB(i, j, pixel);
                    }
                }

                ImageIO.write(output_img, "png", output);

                System.out.println("Image copiée pixel par pixel : " + output.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Copie pixel par pixel avec noir et blanc(1.3
     */
    public void copierN(String path) {
        File file = new File(path);
        if (file.exists()) {
            try {
                BufferedImage img = ImageIO.read(file);

                File output = new File(file.getParent(), "copieN_" + file.getName());
                BufferedImage output_img = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

                for (int i = 0; i < img.getWidth(); i++) {
                    for (int j = 0; j < img.getHeight(); j++) {
                        int pixel = img.getRGB(i, j);

                        int[] colors = OutilCouleur.getTabColor(pixel);
                        int new_colors = ((colors[0] + colors[1] + colors[2]) / 3);

                        int new_pixel = OutilCouleur.getCouleur(new_colors, new_colors, new_colors);
                        output_img.setRGB(i, j, new_pixel);
                    }
                }

                ImageIO.write(output_img, "png", output);

                System.out.println("Image copiée pixel par pixel : " + output.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Copie pixel par pixel avec noir et blanc(1.3
     */
    public void copierFiltre(String path, int filter) {
        File file = new File(path);
        if (file.exists()) {
            try {
                BufferedImage img = ImageIO.read(file);

                File output = new File(file.getParent(), "copieFiltre_" + file.getName());
                BufferedImage output_img = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

                for (int i = 0; i < img.getWidth(); i++) {
                    for (int j = 0; j < img.getHeight(); j++) {
                        int pixel = img.getRGB(i, j);
                        int new_pixel = pixel & filter;
                        output_img.setRGB(i, j, new_pixel);
                    }
                }

                ImageIO.write(output_img, "png", output);

                System.out.println("Image copiée pixel par pixel : " + output.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Copie pixel par pixel avec noir et blanc(1.3
     */
    public void copierReduce(String path, Color[] reduce) {
        File file = new File(path);
        if (file.exists()) {
            try {
                BufferedImage img = ImageIO.read(file);

                File output = new File(file.getParent(), "copieReduce_" + file.getName());
                BufferedImage output_img = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

                for (int i = 0; i < img.getWidth(); i++) {
                    for (int j = 0; j < img.getHeight(); j++) {
                        int pixel = img.getRGB(i, j);

                        int[] c1 = OutilCouleur.getTabColor(pixel);

                        int[] c2 = OutilCouleur.getTabColor(reduce[0].getRGB());
                        int[] c3 = OutilCouleur.getTabColor(reduce[1].getRGB());

                        double d1_2 = Math.pow(c1[0] - c2[0], 2) + Math.pow(c1[1] - c2[1], 2) + Math.pow(c1[2] - c2[2], 2);
                        double d1_3 = Math.pow(c1[0] - c3[0], 2) + Math.pow(c1[1] - c3[1], 2) + Math.pow(c1[2] - c3[2], 2);

                        int new_pixel = d1_2 < d1_3 ? reduce[0].getRGB() : reduce[1].getRGB();

                        output_img.setRGB(i, j, new_pixel);
                    }
                }

                ImageIO.write(output_img, "png", output);

                System.out.println("Image copiée pixel par pixel : " + output.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Copie pixel par pixel avec noir et blanc(1.3
     */
    public void copierPalette(String path, Palette palette) {
        File file = new File(path);
        if (file.exists()) {
            try {
                BufferedImage img = ImageIO.read(file);

                File output = new File(file.getParent(), palette.norme.getClass().getName() + "_" + file.getName());

                BufferedImage output_img = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

                for (int i = 0; i < img.getWidth(); i++) {
                    for (int j = 0; j < img.getHeight(); j++) {
                        int pixel = img.getRGB(i, j);
                        output_img.setRGB(i, j, palette.getPlusProcheNorme(new Color(pixel)).getRGB());
                    }
                }

                ImageIO.write(output_img, "png", output);

                System.out.println("Image copiée pixel par pixel : " + output.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}



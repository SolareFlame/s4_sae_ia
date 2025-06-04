import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public static void main() throws IOException {
    File fichierSource = new File("./res/Planete 1.jpg");

    // Vérifier que le fichier source existe

    BufferedImage image = ImageIO.read(fichierSource);
    BufferedImage imageFloue = Gaussien.appliquerFlouGaussien(image, 5, 1.0);

    boolean success = ImageIO.write(imageFloue, "PNG", new File("./res/Planete 1 flou.png"));

    if (success) {
        System.out.println("Image floue générée avec succès : " + "./res/Planete 1 flou.png");
    } else {
        System.out.println("Échec de l'écriture de l'image");
    }
}

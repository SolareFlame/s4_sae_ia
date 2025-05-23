import java.awt.*;

public class Main {

    public static void main(String[] args) {
        CopieImage cp = new CopieImage();

        //cp.copierFiltre("res/fleur.png", 0x00FFFF);
        //cp.copierReduce("res/fleur.png", new Color[]{Color.GREEN, Color.RED});

        NormeCouleurs[] normes = {
                new NormeRedmean(),
                new NormeCIELAB(),
        };


        for (NormeCouleurs nc : normes) {
            System.out.println("Norme : " + nc.getClass().getName());

            Color[] colors = new Color[256];

            int index = 0;
            int[] levels = {0, 51, 102, 153, 204, 255};

            for (int r : levels) {
                for (int g : levels) {
                    for (int b : levels) {
                        colors[index++] = new Color(r, g, b);
                    }
                }
            }

            for (int i = 0; i < 40; i++) {
                int gray = 8 + i * 247 / 39;
                colors[index++] = new Color(gray, gray, gray);
            }

            Palette palette = new Palette(colors, nc);

            /*
            Palette palette = new Palette(new Color[]{
                    Color.decode("#03071e"),
                    Color.decode("#6a040f"),
                    Color.decode("#d00000"),
                    Color.decode("#f48c06"),
                    Color.decode("#ffba08"),


                    Color.decode("#007f5f"),
                    Color.decode("#55a630"),
                    Color.decode("#aacc00"),
                    Color.decode("#d4d700"),
                    Color.decode("#eeef20"),

            }, nc);
            */

            cp.copierPalette("res/fleur.png", palette);
        }

        CompareImage cmp = new CompareImage();

        double distance = cmp.comparer("res/NormeCIELAB_fleur.png", "res/NormeRedmean_fleur.png");
        System.out.println("Distance entre les deux images : " + distance);

        distance = cmp.comparer("res/NormeCIELAB_fleur.png", "res/fleur.png");
        System.out.println("Distance entre les deux images : " + distance);

        distance = cmp.comparer("res/NormeRedmean_fleur.png", "res/fleur.png");
        System.out.println("Distance entre les deux images : " + distance);

        distance = cmp.comparer("res/fleur_1px.png", "res/fleur.png");
        System.out.println("Distance entre les deux images : " + distance);

        distance = cmp.comparer("res/fleur.png", "res/fleur.png");
        System.out.println("Distance entre les deux images : " + distance);


    }


}

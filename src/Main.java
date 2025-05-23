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

            Palette palette = new Palette(new Color[]{
                    Color.decode("#03071e"),
                    Color.decode("#6a040f"),
                    Color.decode("#d00000"),
                    Color.decode("#f48c06"),
                    Color.decode("#ffba08"),

                    /*

                    Color.decode("#007f5f"),
                    Color.decode("#55a630"),
                    Color.decode("#aacc00"),
                    Color.decode("#d4d700"),
                    Color.decode("#eeef20"),
                    */
            }, nc);

            cp.copierPalette("res/fleur.png", palette, "");
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

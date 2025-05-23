import java.awt.*;

public class MainCompare {

    public static void main(String[] args) {
        CopieImage cp = new CopieImage();

        Palette palette1 = new Palette(new Color[]{
                Color.decode("#03071e"),
                Color.decode("#6a040f"),
                Color.decode("#d00000"),
                Color.decode("#f48c06"),
                Color.decode("#ffba08"),
        }, new NormeCIELAB());


        Palette palette2 = new Palette(new Color[]{
                Color.decode("#007f5f"),
                Color.decode("#55a630"),
                Color.decode("#aacc00"),
                Color.decode("#d4d700"),
                Color.decode("#eeef20"),
        }, new NormeCIELAB());

        Palette palette3 = new Palette(new Color[]{
                Color.decode("#264653"),
                Color.decode("#2a9d8f"),
                Color.decode("#e9c46a"),
                Color.decode("#f4a261"),
                Color.decode("#e76f51"),
        }, new NormeCIELAB());




        cp.copierPalette("res/fleur.png", palette1, "p1");
        cp.copierPalette("res/fleur.png", palette2, "p2");
        cp.copierPalette("res/fleur.png", palette3, "p3");


        CompareImage cmp = new CompareImage();

        double distance = cmp.comparer("res/fleur.png", "res/p1_fleur.png");
        System.out.println("Distance entre les deux images : " + distance);

        distance = cmp.comparer("res/fleur.png", "res/p2_fleur.png");
        System.out.println("Distance entre les deux images : " + distance);

        distance = cmp.comparer("res/fleur.png", "res/p3_fleur.png");
        System.out.println("Distance entre les deux images : " + distance);


    }


}

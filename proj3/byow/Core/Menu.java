package byow.Core;

import edu.princeton.cs.introcs.StdDraw;
import java.awt.*;

public class Menu {
    public final static Font TITLE_FONT = new Font("Arial", Font.BOLD, 30);
    public Menu() {
        StdDraw.setFont(TITLE_FONT);
        StdDraw.text(0.5, 0.85, "FAKE POKEMON GAME");
        StdDraw.setFont();
        StdDraw.text(0.5, 0.55, "New Game (N)");
        StdDraw.text(0.5, 0.50, "Load Game (L)");
        StdDraw.text(0.5, 0.45, "Quit (Q)");
        StdDraw.show();
    }

    public static void main(String[] args) {
        Menu test = new Menu();
    }
}

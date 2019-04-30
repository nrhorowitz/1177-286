package byow.Core;

import edu.princeton.cs.introcs.StdDraw;

public class InputKey implements Inputs {


    public char getNextKey() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toUpperCase(StdDraw.nextKeyTyped());
                c = Character.toUpperCase(c);
                return c;
            }
        }
    }

    public boolean possibleNextInput() {
        return true;
    }
}

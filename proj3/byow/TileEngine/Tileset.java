package byow.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile AVATAR = new TETile('@', Color.white, Color.black, "you");
    public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            "wall");
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");

    //Pokemon Themed
    public static final String A_PREFIX_PATH =
            "/Users/coolkid/Desktop/18Newbie/CS/cs61b/sp19-s286/1177-286/proj3/byow/Core/Resources/";
    public static final String N_PREFIX_PATH =
            "/Users/nhorowitz/Desktop/work/cs61b/sp19-s1177/proj3/proj3/byow/Core/Resources/";


    //Order - left, top, right, bot
    public static final TETile FLOOR_A_0000 =
            new TETile('f', Color.GREEN, Color.GREEN, "Floor", A_PREFIX_PATH + "FLOOR_A_0000.png");
    public static final TETile WALL_A_0000 =
            new TETile('w', Color.BLACK, Color.BLACK, "Wall", A_PREFIX_PATH + "WALL_A_0000.png");
    public static final TETile EMPTY_A_0000 =
            new TETile('e', Color.BLUE, Color.BLUE, "Empty", A_PREFIX_PATH + "EMPTY_A_0000.png");
}



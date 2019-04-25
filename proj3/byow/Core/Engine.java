package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import edu.princeton.cs.algs4.StdRandom;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    Map<Integer, String> rooms;
    int totalSectors;



    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.


        // 0) Set random seed from input
        // 1) Fill everything with nothing (water XD)
        // 2) Given width and height, create a 2 dimensional int array of zones
        // n as number of rooms   --- dependency on distribution ?
        // 3) Parameters for room dimensions (by floor)   //master data
        // 4) Parameters for hallways between sectors     //master data
        // 5) Add floors  (to final world frame)
        // 6) Add walls (option for inefficiency)  helper adjacent  (to final world frame)
        // 7) Big flex owo

        this.seed(input);  // 0)
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        fillWater(finalWorldFrame);  // 1) done
        int[][] numRoomSector = numRoomSector();  // 2)
        rooms = computeRoom(numRoomSector);  // 3)
        List<String> hallCoordinates = computeHall(numRoomSector);  // 4)
        addFloors(finalWorldFrame);  // 5)
        addWalls(finalWorldFrame);  // 6)
        addWater(finalWorldFrame);  // 7)
        return finalWorldFrame;
    }

    /**
     * Seed sets StdRandom to the input to create pseudorandom generation.
     * @param input of primitive type String
     */
    private void seed(String input) {
        long l = (long) input.hashCode();
        StdRandom.setSeed(l);
    }

    /**
     * FillWater initializes world with default water tiles.
     * @param world of type TETile[][]
     */
    private void fillWater(TETile[][] world) {
        int width = world.length;
        int height = world[0].length;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                world[x][y] = Tileset.EMPTY_A_0;
            }
        }
    }

    /**
     * NumRoomSector seeds each sector with a random number of rooms.
     * Rooms may overlap
     * **make sure rooms are connected for n > 1 rooms
     * @return array of room counts by sector of type int[][]
     */
    private int[][] numRoomSector() {
        int width = StdRandom.uniform(6, 9);
        int height = StdRandom.uniform(3, 5);
        totalSectors = width * height;
        int[][] returnArray = new int[height][width];
        for (int y = 0; y < height; y += 1) {
            for (int x = 0; x < width; x += 1) {
                boolean zero = StdRandom.uniform(0, 2) == 0;
                if (zero) {
                    returnArray[y][x] = 0;
                } else {
                    returnArray[y][x] = StdRandom.uniform(1, 5);
                }
            }
        }
        return returnArray;
    }

    /**
     * ComputeRoom returns mapping of sector index to string containing coordinates.
     * @param numRoomSector number of rooms per sector of type int[][]
     * @return mapping of type Map<Integer, String>
     */
    private Map<Integer, String> computeRoom(int[][] numRoomSector) {
        Map<Integer, String> returnMap = new HashMap<>();
        int h = numRoomSector.length;
        int w = numRoomSector[0].length;
        int hBound = HEIGHT / h;
        int hRem = HEIGHT - (h * hBound); //extra length to add to one box
        int wBound = WIDTH / w;
        int wRem = WIDTH - (w * wBound); //extra length to add to one box
        int hExtendedIndex = StdRandom.uniform(0, h);
        int wExtendedIndex = StdRandom.uniform(0, w);
        //create boxes (y,x)
        for (int i = 0; i < h; i += 1) {
            for (int j = 0; j < w; j += 1) {
                for (int count = 0; count < numRoomSector[i][j]; count += 1) {
                    int bot = StdRandom.uniform((i * hBound) + 1, ((i + 1) * hBound) - 4);
                    int left = StdRandom.uniform((j * wBound) + 1, ((j + 1) * wBound) - 4);
                    int top = StdRandom.uniform(bot + 1, ((i + 1) * hBound) - 1);
                    int right = StdRandom.uniform(left + 1, ((j + 1) * wBound) - 1);
                    String temp = bot + "_" + left + "_" + top + "_" + right + "_";
                    if (returnMap.containsKey((w * i) + j)) {
                        String og = returnMap.get((w * i) + j).substring(0, 8);
                        while (!overlap(og, temp)) {
                            bot = StdRandom.uniform((i * hBound) + 1, ((i + 1) * hBound) - 4);
                            left = StdRandom.uniform((j * wBound) + 1, ((j + 1) * wBound) - 4);
                            top = StdRandom.uniform(bot + 1, ((i + 1) * hBound) - 1);
                            right = StdRandom.uniform(left + 1, ((j + 1) * wBound) - 1);
                            temp = bot + "_" + left + "_" + top + "_" + right + "_";
                        }
                        returnMap.put((w * i) + j, returnMap.get((w * i) + j) + temp);
                    } else {
                        returnMap.put((w * i) + j, temp);
                    }
                }
            }
        }
        return returnMap;
    }

    /**
     * Overlap returns whether or not the two rooms overlap each other.
     * @param first first room in the query
     * @param second second room in the query
     * @return boolean, true if overlap false if doesn't
     */
    private static boolean overlap(String first, String second) {
        String[] firstData = first.split("_");
        String[] secondData = second.split("_");
        int firstB = Integer.parseInt(firstData[0]);
        int firstL = Integer.parseInt(firstData[1]);
        int firstT = Integer.parseInt(firstData[2]);
        int firstR = Integer.parseInt(firstData[3]);

        int secondB = Integer.parseInt(secondData[0]);
        int secondL = Integer.parseInt(secondData[1]);
        int secondT = Integer.parseInt(secondData[2]);
        int secondR = Integer.parseInt(secondData[3]);

        if (Math.min(firstT, secondT) > Math.max(firstB, secondB)
                && Math.min(firstR, secondR) > Math.max(firstL, secondL)) {
            return true;
        }
        return false;
    }

    /**
     * Each index corresponds to a room.
     * 0+2h ....
     * 0+h 1+h 2+h ...
     * 0   1   2   ...
     * @return
     */
    private List<String> computeHall(int[][] numRoomSector) {
        /*
        int h = numRoomSector.length;
        int w = numRoomSector[0].length;
        int hBound = HEIGHT / h;
        int hRem = HEIGHT - (h * hBound); //extra length to add to one box
        int wBound = WIDTH / w;
        int wRem = WIDTH - (w * wBound); //extra length to add to one box
        //all branches horizontal
        //only three vertical
        for (int i = 0; i < w * h; i += w) {
            if (rooms.containsKey(i)) {
                String roomData = rooms.get(i);
                String[] roomDataArray = roomData.split("_");
                int startMinBot = 1000;
                int startMaxTop = 0;
                for (int j = 0; j < roomDataArray.length; j += 1) {
                    if (Math.floorMod(j, 4) == 0) { //bot
                        if (bot < startMinBot) {
                            startMinBot = roomDataArray[j];
                        }
                    } else if (Math.floorMod(j, 4) == 2) { //top
                        if (top > startMaxBot) {
                            startMaxBot = roomDataArray[j];
                        }
                    }
                }
                //check for room after
            }

        }*///dont have to use, this approach is probably bad

        return new LinkedList<String>();
    }

    /**
     * AddFloors adds floor tiles to master tile array.
     * @param finalWorldFrame of type TETile[][]
     */
    private void addFloors(TETile[][] finalWorldFrame) {
        for (int i = 0; i < totalSectors; i += 1) {
            if (rooms.containsKey(i)) {
                String roomData = rooms.get(i);
                String[] roomDataArray = roomData.split("_");
                for (int j = 0; j < roomDataArray.length; j += 4) {
                    int bot = Integer.parseInt(roomDataArray[j + 0]);
                    int left = Integer.parseInt(roomDataArray[j + 1]);
                    int top = Integer.parseInt(roomDataArray[j + 2]);
                    int right = Integer.parseInt(roomDataArray[j + 3]);
                    for (int y = bot; y <= top; y += 1) {
                        for (int x = left; x <= right; x += 1) {
                            finalWorldFrame[x][y] = Tileset.FLOOR_A_0;
                        }
                    }
                }
            }
        }
    }

    /**
     * Adjacent returns array of size 4
     * Order - left, top, right, bot
     * 0 - nothing
     * 1 - a wall
     * 2 - a floor
     * @param finalWorldFrame of type TETile[][]
     * @param h height of current index of primitive type int
     * @param w width of current index of primitive type int
     * @return adjacents of type int[]
     */
    private int[] adjacent(TETile[][] finalWorldFrame, int h, int w) {
        int[] returnArray = new int[4];
        returnArray[0] = 0;
        returnArray[1] = 0;
        returnArray[2] = 0;
        returnArray[3] = 0;
        if (w - 1 >= 0) {
            if (finalWorldFrame[w - 1][h].description().equals("Floor")) {
                returnArray[0] = 2;
            } else if (finalWorldFrame[w - 1][h].description().equals("Wall")) {
                returnArray[0] = 1;
            }
        }
        if (h + 1 < HEIGHT) {
            if (finalWorldFrame[w][h + 1].description().equals("Floor")) {
                returnArray[1] = 2;
            } else if (finalWorldFrame[w][h + 1].description().equals("Wall")) {
                returnArray[1] = 1;
            }
        }
        if (w + 1 < WIDTH) {
            if (finalWorldFrame[w + 1][h].description().equals("Floor")) {
                returnArray[2] = 2;
            } else if (finalWorldFrame[w + 1][h].description().equals("Wall")) {
                returnArray[2] = 1;
            }
        }
        if (h - 1 >= 0) {
            if (finalWorldFrame[w][h - 1].description().equals("Floor")) {
                returnArray[3] = 2;
            } else if (finalWorldFrame[w][h - 1].description().equals("Wall")) {
                returnArray[3] = 1;
            }
        }
        return returnArray;
    }

    /**
     * AddWalls adds wall tiles to master tile array.
     * Depends on placements of walls
     * Two passes, one to place walls one to render orientation
     * @param finalWorldFrame of type TETile[][]
     */
    private void addWalls(TETile[][] finalWorldFrame) {
        for (int h = 0; h < HEIGHT; h += 1) {
            for (int w = 0; w < WIDTH; w += 1) {
                if (!(finalWorldFrame[w][h].description().equals("Floor"))) {
                    int[] adjacentData = adjacent(finalWorldFrame, h, w);
                    boolean foundFloor = false;
                    for (int direction = 0; direction < 4; direction += 1) {
                        if (adjacentData[direction] == 2) {
                            foundFloor = true;
                        }
                    }
                    if (foundFloor) {
                        finalWorldFrame[w][h] = Tileset.WALL_A_2_LR;
                    }
                }
            }
        }
    }

    /**
     * AddWater adds water tiles texture to master tile array.
     * Depends on placements of walls
     * @param finalWorldFrame of type TETile[][]
     */
    private void addWater(TETile[][] finalWorldFrame) {
        return;
    }

    public static void main(String[] args) {
        System.out.println(overlap("4_4_6_6_", "4_4_5_5_"));
    }
}

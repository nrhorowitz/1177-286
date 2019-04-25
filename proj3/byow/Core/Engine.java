package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import edu.princeton.cs.algs4.StdRandom;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import java.awt.Color;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    Map<Integer, String> rooms;
    Set<String> pivots;
    Set<String> copy;
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
        //List<String> hallCoordinates = computeHall(numRoomSector);  // 4)

        addFloors(finalWorldFrame);  // 5)
        addHalls(numRoomSector, finalWorldFrame);
        addWalls(finalWorldFrame);  // 6)
        addWalls(finalWorldFrame);  // 6) for textures
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
                world[x][y] = Tileset.EMPTY_A_0000;
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
        pivots = new HashSet<String>();
        copy = new HashSet<String>();
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
                int bot0 = 0;
                int left0 = 0;
                int top0 = 0;
                int right0 = 0;
                for (int count = 0; count < numRoomSector[i][j]; count += 1) {
                    int bot = StdRandom.uniform((i * hBound) + 1, ((i + 1) * hBound) - 4);
                    int left = StdRandom.uniform((j * wBound) + 1, ((j + 1) * wBound) - 4);
                    int top = StdRandom.uniform(bot + 1, ((i + 1) * hBound) - 1);
                    int right = StdRandom.uniform(left + 1, ((j + 1) * wBound) - 1);
                    String temp = bot + "_" + left + "_" + top + "_" + right + "_";
                    if (count == 0) { //set first square
                        bot0 = bot;
                        left0 = left;
                        top0 = top;
                        right0 = right;
                        //assign a random tile as the pivot that is connected
                        int pivotH = StdRandom.uniform(bot, top + 1);
                        int pivotW = StdRandom.uniform(left, right + 1);
                        pivots.add(pivotH + "_" + pivotW);
                        copy.add(pivotH + "_" + pivotW);
                    }
                    if (returnMap.containsKey((w * i) + j)) {
                        returnMap.put((w * i) + j, returnMap.get((w * i) + j) + temp);
                    } else {
                        returnMap.put((w * i) + j, temp);
                    }
                    //append extra box to ensure connectivity
                    //find minimum distance of opposite points then construct box (inclusive)
                    int originDistance = Math.abs(left0 - right) + Math.abs(bot0 - top);
                    int desDistance = Math.abs(right0 - left) + Math.abs(top0 - bot);
                    if (originDistance < desDistance) {
                        int aBot = Math.min(bot0, top);
                        int aTop = Math.max(bot0, top);
                        int aLeft = Math.min(left0, right);
                        int aRight = Math.max(left0, right);
                        String a = aBot + "_" + aLeft + "_" + aTop + "_" + aRight + "_";
                        returnMap.put((w * i) + j, returnMap.get((w * i) + j) + a);
                    }
                }
            }
        }
        return returnMap;
    }

    /**
     * Have a set of connected rooms, each time append a room, attach pivot to a rando in set.
     * @return
     */
    private void addHalls(int[][] numRoomSector, TETile[][] finalWorldFrame) {
        Set<String> linkedPivots = new HashSet<String>();
        //Set one to the starting pivot in link.
        Iterator<String> pivotsIT = pivots.iterator();
        String temp = pivotsIT.next();
        pivots.remove(temp);
        linkedPivots.add(temp);
        while (!pivots.isEmpty()) {
            //Pick starting pivot not in set.
            pivotsIT = pivots.iterator();
            int randPivotStartIndex = StdRandom.uniform(1, pivots.size() + 1);
            String randPivotStart = null;

            for (int i = 0; i < randPivotStartIndex; i += 1) {
                randPivotStart = pivotsIT.next();
            }
            pivots.remove(randPivotStart);
            //Pick ending pivot in set.
            Iterator<String> linkedPivotsIT = linkedPivots.iterator();
            int randPivotEndIndex = StdRandom.uniform(1, linkedPivots.size() + 1);
            String randPivotEnd = null;
            for (int i = 0; i < randPivotEndIndex; i += 1) {
                randPivotEnd = linkedPivotsIT.next();
            }
            //Pick a random turning point for hallway
            //construct in two intervals
            String[] start = randPivotStart.split("_"); //bottop_leftright
            String[] end = randPivotEnd.split("_"); //bottop_leftright
            int starty = Integer.parseInt(start[0]);
            int endy = Integer.parseInt(end[0]);
            int startx = Integer.parseInt(start[1]);
            int endx = Integer.parseInt(end[1]);

            int left = Math.min(startx, endx);
            int bot = Math.min(starty, endy);
            int right = Math.max(startx, endx);
            int top = Math.max(starty, endy);
            if ((startx < endx && starty < endy) || (endx < startx && endy < starty)) {
                if (StdRandom.uniform(0, 2) == 0) {
                    for (int i = left; i <= right; i += 1) {
                        finalWorldFrame[i][bot] = Tileset.FLOOR_A_0000;
                    }
                    for (int j = bot; j <= top; j += 1) {
                        finalWorldFrame[right][j] = Tileset.FLOOR_A_0000;
                    }
                } else {
                    for (int j = bot; j <= top; j += 1) {
                        finalWorldFrame[left][j] = Tileset.FLOOR_A_0000;
                    }
                    for (int i = left; i <= right; i += 1) {
                        finalWorldFrame[i][top] = Tileset.FLOOR_A_0000;
                    }
                }
            } else {
                if (StdRandom.uniform(0, 2) == 0) {
                    for (int i = left; i <= right; i += 1) {
                        finalWorldFrame[i][top] = Tileset.FLOOR_A_0000;
                    }
                    for (int j = bot; j <= top; j += 1) {
                        finalWorldFrame[right][j] = Tileset.FLOOR_A_0000;
                    }
                } else {
                    for (int j = bot; j <= top; j += 1) {
                        finalWorldFrame[right][j] = Tileset.FLOOR_A_0000;
                    }
                    for (int i = left; i <= right; i += 1) {
                        finalWorldFrame[i][top] = Tileset.FLOOR_A_0000;
                    }
                }
            }

        }
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
                            finalWorldFrame[x][y] = Tileset.FLOOR_A_0000;
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
                    //Textures
                    int[] adjacentData = adjacent(finalWorldFrame, h, w);
                    String texture = "";
                    boolean foundFloor = false;
                    for (int direction = 0; direction < 4; direction += 1) {
                        if (adjacentData[direction] == 2) {
                            foundFloor = true;
                            texture += "0";
                        } else if (adjacentData[direction] == 1) {
                            texture += "1";
                        } else {
                            texture += "0";
                        }
                    }
                    if (foundFloor) {
                        char p1 = Tileset.WALL_A_0000.character();
                        Color p2 = Color.BLACK;
                        Color p3 = p2;
                        String p4 = Tileset.WALL_A_0000.description();
                        String p5 = Tileset.N_PREFIX_PATH + "WALL_A_" + texture + ".png";
                        TETile add = new TETile(p1, p2, p3, p4, p5);
                        finalWorldFrame[w][h] = add;
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
        for (int h = 0; h < HEIGHT; h += 1) {
            for (int w = 0; w < WIDTH; w += 1) {
                if (finalWorldFrame[w][h].description().equals("Empty")) {
                    //Textures
                    int[] adjacentData = adjacent(finalWorldFrame, h, w);
                    String texture = "";
                    for (int direction = 0; direction < 4; direction += 1) {
                        if (adjacentData[direction] == 1) {
                            texture += "1";
                        } else {
                            texture += "0";
                        }
                    }
                    char p1 = Tileset.EMPTY_A_0000.character();
                    Color p2 = Color.BLUE;
                    Color p3 = p2;
                    String p4 = Tileset.EMPTY_A_0000.description();
                    String p5 = Tileset.N_PREFIX_PATH + "EMPTY_A_" + texture + ".png";
                    TETile add = new TETile(p1, p2, p3, p4, p5);
                    finalWorldFrame[w][h] = add;
                }
            }
        }
        return;
    }
}

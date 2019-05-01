package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class AvatarCharacter implements Characters {
    private String name;
    private TETile[] tiles; //0 = left, 1 = top, 2 = right, 3 = bot
    private String data; //Y_X_lives

    public AvatarCharacter() {
        name = "Avatar";
        tiles = new TETile[4];
        tiles[0] = Tileset.AVATAR_A_0;
        tiles[1] = Tileset.AVATAR_A_1;
        tiles[2] = Tileset.AVATAR_A_2;
        tiles[3] = Tileset.AVATAR_A_3;
    }

    @Override
    public String getName() {
        return name;
    }
    @Override
    public TETile[] getTiles() {
        return tiles;
    }
    @Override
    public String getLocation() {
        return data;
    }
    @Override
    public void setLocation(String data) {
        this.data = data;
    }
}

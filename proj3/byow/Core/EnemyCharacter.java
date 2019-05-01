package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class EnemyCharacter implements Characters {
    private String name;
    private TETile[] tiles;
    private String data;

    public EnemyCharacter() {
        name = "Enemy";
        tiles = new TETile[4];
        tiles[0] = Tileset.ENEMY_A_0;
        tiles[1] = Tileset.ENEMY_A_1;
        tiles[2] = Tileset.ENEMY_A_2;
        tiles[3] = Tileset.ENEMY_A_3;
    }
    public String getName() {
        return name;
    }

    public TETile[] getTiles() {
        return tiles;
    }

    public String getLocation() {
        return data;
    }

    public void setLocation(String data) {
        this.data = data;
    }
}

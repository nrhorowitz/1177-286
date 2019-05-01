package byow.Core;

import byow.TileEngine.TETile;

public interface Characters {
    String getName();
    TETile[] getTiles();
    String getLocation();
    void setLocation(String data);
}

package engine.terrain;

import engine.water.Water;
import engine.water.WaterTile;
import maths.joml.Vector3f;

public class FlatWorld extends World {

    public FlatWorld(float terrainSize) {
        super(terrainSize);
    }

    @Override
    protected Terrain generateTerrain(Vector3f position) {
        return new SimpleTerrain(position, getTerrainSize());
    }

    @Override
    protected Water generateWater(Terrain terrain) {
        return new WaterTile(terrain);
    }
}

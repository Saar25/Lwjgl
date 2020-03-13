package engine.terrain.lowpoly;

import engine.terrain.Terrain;
import engine.terrain.World;
import engine.water.Water;
import engine.water.lowpoly.LowpolyWavyWater;
import maths.joml.Vector3f;

public class LowPolyWorld extends World {

    private final LPTerrainConfigs configs;

    public LowPolyWorld(float terrainSize) {
        super(terrainSize);
        this.configs = null;
    }

    public LowPolyWorld(LPTerrainConfigs configs) {
        super(configs.size);
        this.configs = configs;
    }

    @Override
    protected Terrain generateTerrain(Vector3f position) {
        if (configs != null) configs.position.set(position);
        return configs != null ? new LowPolyTerrain(configs)
                : new LowPolyTerrain(position, getTerrainSize());
    }

    @Override
    protected Water generateWater(Terrain terrain) {
        return new LowpolyWavyWater(terrain);
    }
}

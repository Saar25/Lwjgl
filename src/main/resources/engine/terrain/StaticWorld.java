package engine.terrain;

import engine.water.Water;
import engine.water.WaterTile;
import maths.joml.Vector3f;

public class StaticWorld extends World {

    public StaticWorld(Terrain... terrains) {
        super(1);
        for (Terrain terrain : terrains) {
            getChildren().add(terrain);
        }
    }

    @Override
    protected Terrain generateTerrain(Vector3f position) {
        throw new UnsupportedOperationException("Cannot add new terrains to StaticWorld");
    }

    @Override
    protected Water generateWater(Terrain terrain) {
        return new WaterTile(terrain);
    }

    @Override
    public void addTerrain(Vector3f position) {
        throw new UnsupportedOperationException("Cannot add new terrains to StaticWorld");
    }

    @Override
    public void setGeneration(ProceduralGeneration generation) {
        throw new UnsupportedOperationException("Cannot add new terrains to StaticWorld");
    }
}

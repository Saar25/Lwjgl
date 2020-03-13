package engine.terrain.lowpoly;

import engine.terrain.generation.ColourGenerator;
import engine.terrain.generation.HeightGenerator;
import maths.joml.Vector3f;
import maths.utils.Vector3;

public class LPTerrainConfigs {

    HeightGenerator heightGenerator;
    ColourGenerator colourGenerator;

    Vector3f position = Vector3.create();
    float size;

    int vertices = 128;

    int[] levels = {};

    public LPTerrainConfigs setHeightGenerator(HeightGenerator heightGenerator) {
        this.heightGenerator = heightGenerator;
        return this;
    }

    public LPTerrainConfigs setColourGenerator(ColourGenerator colourGenerator) {
        this.colourGenerator = colourGenerator;
        return this;
    }

    public LPTerrainConfigs setPosition(Vector3f position) {
        this.position = position;
        return this;
    }

    public LPTerrainConfigs setSize(float size) {
        this.size = size;
        return this;
    }

    public LPTerrainConfigs setVertices(int vertices) {
        this.vertices = vertices;
        return this;
    }

    public LPTerrainConfigs setLevels(int... levels) {
        this.levels = levels;
        return this;
    }
}

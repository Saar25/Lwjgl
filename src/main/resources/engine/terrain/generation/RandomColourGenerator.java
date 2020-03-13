package engine.terrain.generation;

import engine.terrain.TerrainVertex;
import maths.joml.Vector3f;
import maths.utils.Vector3;

public class RandomColourGenerator implements ColourGenerator {

    @Override
    public Vector3f generateColour(TerrainVertex vertex) {
        return Vector3.create().set((float) Math.random(), (float) Math.random(), (float) Math.random());
    }
}

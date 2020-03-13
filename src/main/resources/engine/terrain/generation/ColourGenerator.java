package engine.terrain.generation;

import engine.terrain.TerrainVertex;
import maths.joml.Vector3f;

public interface ColourGenerator {

    Vector3f generateColour(TerrainVertex vertex);

}

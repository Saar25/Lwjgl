package engine.terrain.generation;

import engine.models.Model;
import maths.joml.Vector3fc;

public interface TerrainModelGenerator {

    Model generateModel(Vector3fc position, float size);

}
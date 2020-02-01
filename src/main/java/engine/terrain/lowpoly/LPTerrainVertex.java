package engine.terrain.lowpoly;

import engine.terrain.TerrainVertex;
import maths.joml.Vector2f;
import maths.joml.Vector3f;

public class LPTerrainVertex implements TerrainVertex {

    private final Vector3f position;
    private final Vector3f normal;

    public LPTerrainVertex(Vector3f position, Vector3f normal) {
        this.position = position;
        this.normal = normal;
    }

    @Override
    public Vector3f getPosition() {
        return position;
    }

    @Override
    public Vector3f getNormal() {
        return normal;
    }

    @Override
    public Vector2f getTexCoords() {
        return null;
    }
}

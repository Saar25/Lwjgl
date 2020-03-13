package engine.terrain;

import maths.joml.Vector2f;
import maths.joml.Vector3f;

public interface TerrainVertex {

    Vector3f getPosition();

    Vector3f getNormal();

    Vector2f getTexCoords();

}

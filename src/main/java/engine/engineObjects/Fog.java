package engine.engineObjects;

import maths.joml.Vector3f;
import maths.utils.Vector3;

public class Fog {

    public static final Fog NONE = new Fog(-1, -1, Vector3.of(1));

    private final float minDistance;
    private final float maxDistance;
    private final Vector3f colour;

    public Fog(float minDistance, float maxDistance, Vector3f colour) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.colour = colour;
    }

    public Fog(float minDistance, float maxDistance) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.colour = Vector3.create();
    }

    public float getMinDistance() {
        return minDistance;
    }

    public float getMaxDistance() {
        return maxDistance;
    }

    public Vector3f getColour() {
        return colour;
    }
}

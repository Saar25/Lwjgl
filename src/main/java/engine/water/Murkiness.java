package engine.water;

import maths.joml.Vector3f;
import maths.joml.Vector3fc;
import maths.utils.Vector3;

public class Murkiness {

    public static final Murkiness NONE = null;

    private final Vector3f colour;
    private final float min;
    private final float max;
    private final float depth;

    public Murkiness(Vector3fc colour, float min, float max, float depth) {
        this.colour = Vector3.of(colour);
        this.min = min;
        this.max = max;
        this.depth = depth;
    }

    public Vector3f getColour() {
        return colour;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    public float getDepth() {
        return depth;
    }
}

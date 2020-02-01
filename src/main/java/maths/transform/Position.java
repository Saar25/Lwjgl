package maths.transform;

import maths.utils.Vector3;
import maths.joml.Vector3f;

public class Position {

    private final Vector3f value;

    private Position(Vector3f value) {
        this.value = value;
    }

    public static Position of(float x, float y, float z) {
        Vector3f value = Vector3.of(x, y, z);
        return new Position(value);
    }

    public static Position create() {
        Vector3f position = Vector3.create();
        return new Position(position);
    }

    public Vector3f getValue() {
        return value;
    }
}

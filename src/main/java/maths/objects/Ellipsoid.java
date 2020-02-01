package maths.objects;

import maths.utils.Vector3;
import maths.joml.Vector3fc;

public class Ellipsoid {

    private final Vector3fc position;
    private final Vector3fc dimensions;

    public Ellipsoid() {
        this.position = Vector3.of(0, 0, 0);
        this.dimensions = Vector3.of(1, 1, 1);
    }

    public Ellipsoid(Vector3fc position, Vector3fc dimensions) {
        this.position = Vector3.of(position);
        this.dimensions = Vector3.of(dimensions);
    }

    public Vector3fc getPosition() {
        return position;
    }

    public Vector3fc getDimensions() {
        return dimensions;
    }

    public Ellipsoid toSpace(Vector3fc space) {
        if (space.equals(Vector3.ONE, 0.01f)) return this;
        Vector3fc position = Vector3.of(this.position).div(space);
        Vector3fc dimensions = Vector3.of(this.dimensions).div(space);
        return new Ellipsoid(position, dimensions);
    }
}

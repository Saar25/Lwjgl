package maths.objects;

import maths.utils.Vector3;
import maths.joml.Vector3fc;

public class Plane {

    private final Vector3fc normal;
    private final float cp;

    public Plane(Vector3fc normal, float d) {
        float length = Vector3.length(normal);
        this.normal = Vector3.of(normal).div(length);
        this.cp = d / length;
    }

    public Plane(float a, float b, float c, float d) {
        float length = Vector3.length(a, b, c);
        this.normal = Vector3.of(a, b, c).div(length);
        this.cp = d / length;
    }

    public Plane(Vector3fc p1, Vector3fc p2, Vector3fc p3) {
        Vector3fc p21 = Vector3.sub(p2, p1);
        Vector3fc p31 = Vector3.sub(p3, p1);
        this.normal = Vector3.cross(p21, p31).normalize();
        this.cp = -normal.dot(p1);
    }

    public float distance(Vector3fc point) {
        return Math.abs(normal.dot(point) + cp);
    }

    public float signedDistance(Vector3fc point) {
        return normal.dot(point) + cp;
    }

    public boolean isFrontFacing(Vector3fc point) {
        return normal.dot(point) > 0;
    }

    public Vector3fc getNormal() {
        return normal;
    }

    public float getCp() {
        return cp;
    }
}

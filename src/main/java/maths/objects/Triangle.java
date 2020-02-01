package maths.objects;

import maths.joml.Vector2fc;
import maths.utils.Maths;
import maths.utils.Vector3;
import maths.joml.Vector3fc;

public class Triangle {

    private final Vector3fc p1;
    private final Vector3fc p2;
    private final Vector3fc p3;
    private final Plane plane;

    public Triangle(Vector3fc p1, Vector3fc p2, Vector3fc p3) {
        this.p1 = Vector3.of(p1);
        this.p2 = Vector3.of(p2);
        this.p3 = Vector3.of(p3);
        this.plane = new Plane(p1, p2, p3);
    }

    public boolean contains(Vector3fc point) {
        Vector3fc e21 = Vector3.sub(p2, p1);
        Vector3fc e31 = Vector3.sub(p3, p1);
        Vector3fc ep1 = Vector3.sub(point, p1);
        float a = e21.dot(e21);
        float b = e21.dot(e31);
        float c = e31.dot(e31);
        float d = ep1.dot(e21);
        float e = ep1.dot(e31);
        float ac_bb = a * c - b * b;
        int x = (int) (d * c - e * b);
        int y = (int) (e * a - d * b);
        int z = (int) (x + y - ac_bb);
        return ((z & ~(x | y)) & 0x80000000) != 0;
    }

    public boolean contains(float x, float z) {
        float A = .5f * (-p2.z() * p3.x() + p1.z() * (-p2.x() + p3.x()) + p1.x() * (p2.z() - p3.z()) + p2.x() * p3.z());
        float sign = A < 0 ? -1 : 1;
        float s = (p1.z() * p3.x() - p1.x() * p3.z() + (p3.z() - p1.z()) * x + (p1.x() - p3.x()) * z) * sign;
        float t = (p1.x() * p2.z() - p1.z() * p2.x() + (p1.z() - p2.z()) * x + (p2.x() - p1.x()) * z) * sign;
        return s > 0 && t > 0 && (s + t) < 2 * A * sign;
    }

    public float getHeight(Vector2fc position) {
        return Maths.barryCentric(getP1(), getP2(), getP3(), position);
    }

    public Vector3fc getP1() {
        return p1;
    }

    public Vector3fc getP2() {
        return p2;
    }

    public Vector3fc getP3() {
        return p3;
    }

    public Plane getPlane() {
        return plane;
    }

    public Triangle toSpace(Vector3fc space) {
        if (space.equals(Vector3.ONE, 0.01f)) return this;
        Vector3fc p1 = Vector3.of(this.p1).div(space);
        Vector3fc p2 = Vector3.of(this.p2).div(space);
        Vector3fc p3 = Vector3.of(this.p3).div(space);
        return new Triangle(p1, p2, p3);
    }

    @Override
    public String toString() {
        return "[Triangle: " + p1 + ", " + p2 + ", " + p3 + "]";
    }
}

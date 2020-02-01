package engine.collision;

import maths.objects.Ellipsoid;
import maths.objects.Triangle;
import maths.utils.Vector3;
import maths.joml.Vector3fc;

public class CollisionData {

    private final Triangle triangle;
    private final Ellipsoid ellipsoid;
    private final Vector3fc velocity;

    public CollisionData(Triangle triangle, Ellipsoid ellipsoid, Vector3fc velocity) {
        this.triangle = triangle.toSpace(ellipsoid.getDimensions());
        this.ellipsoid = ellipsoid.toSpace(ellipsoid.getDimensions());
        this.velocity = velocity.div(ellipsoid.getDimensions(), Vector3.create());
    }

    public Triangle getTriangle() {
        return triangle;
    }

    public Ellipsoid getEllipsoid() {
        return ellipsoid;
    }

    public Vector3fc getVelocity() {
        return velocity;
    }
}

package engine.collision;

import maths.objects.Ellipsoid;
import maths.utils.Vector3;
import maths.joml.Vector3f;
import maths.joml.Vector3fc;

public class CollisionEvent {

    private final TriangleBatch triangleBatch;
    private final Ellipsoid ellipsoid;
    private final Vector3fc velocity;

    private Vector3f intersectionPoint = Vector3.create();
    private boolean collisionFound = false;
    private float nearestDistance = 0;

    public CollisionEvent(TriangleBatch triangleBatch, Ellipsoid ellipsoid, Vector3fc velocity) {
        this.triangleBatch = triangleBatch;
        this.ellipsoid = ellipsoid;
        this.velocity = velocity;
    }

    public TriangleBatch getTriangleBatch() {
        return triangleBatch;
    }

    public Ellipsoid getEllipsoid() {
        return ellipsoid;
    }

    public Vector3fc getVelocity() {
        return velocity;
    }

    public Vector3fc getIntersectionPoint() {
        return intersectionPoint;
    }

    public void setIntersectionPoint(float x, float y, float z) {
        this.intersectionPoint.set(x, y, z);
    }

    public float getNearestDistance() {
        return nearestDistance;
    }

    public void setNearestDistance(float distance) {
        this.nearestDistance = Math.min(this.nearestDistance, distance);
    }

    public boolean isCollisionFound() {
        return collisionFound;
    }

    public void foundCollision() {
        this.collisionFound = true;
    }
}

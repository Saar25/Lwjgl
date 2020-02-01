package engine.collision;

import engine.util.Pointer;
import maths.objects.Ellipsoid;
import maths.objects.Plane;
import maths.objects.Triangle;
import maths.utils.Maths;
import maths.utils.Vector3;
import maths.joml.Vector3f;
import maths.joml.Vector3fc;

import java.util.List;

public final class Collision {

    private Collision() {
        throw new AssertionError("Cannot make instance of class Collision");
    }

    public static CollisionEvent checkTriangles(TriangleBatch triangleBatch, Ellipsoid ellipsoid, Vector3fc velocity) {
        CollisionEvent collisionEvent = new CollisionEvent(triangleBatch, ellipsoid, velocity);
        checkTriangles(collisionEvent);
        return collisionEvent;
    }

    public static void checkTriangles(CollisionEvent collisionEvent) {
        List<Triangle> triangles = collisionEvent.getTriangleBatch().getTriangles();
        for (Triangle triangle : triangles) {
            checkTriangle(collisionEvent, triangle);
        }
    }

    public static void checkTriangle(CollisionEvent collisionEvent, Triangle triangle) {
        final Ellipsoid ellipsoid = collisionEvent.getEllipsoid();
        final Vector3fc velocity = collisionEvent.getVelocity();
        final Vector3fc basePoint = ellipsoid.getPosition();
        final Vector3fc p1 = triangle.getP1();
        final Vector3fc p2 = triangle.getP2();
        final Vector3fc p3 = triangle.getP3();

        // Make the plane containing this triangle.
        final Plane trianglePlane = triangle.getPlane();
        // Is triangle front-facing to the velocity vector?
        // We only check front-facing triangles
        // (your choice of course)
        Vector3fc normalizedVelocity = Vector3.normalize(velocity);
        if (!trianglePlane.isFrontFacing(normalizedVelocity)) {
            return;
        }
        // Get interval of plane intersection:
        float t0, t1;
        boolean embeddedInPlane = false;
        // Calculate the signed distance from sphere
        // position to triangle plane
        float signedDistToTrianglePlane =
                trianglePlane.signedDistance(basePoint);
        // cache this as we’re going to use it a few times below:
        float normalDotVelocity =
                trianglePlane.getNormal().dot(velocity);
        // if sphere is travelling parallel to the plane:
        if (normalDotVelocity == 0.0f) {
            if (Math.abs(signedDistToTrianglePlane) >= 1.0f) {
                // Sphere is not embedded in plane.
                // No collision possible:
                return;
            } else {
                // sphere is embedded in plane.
                // It intersects in the whole range [0..1]
                embeddedInPlane = true;
                t0 = 0.0F;
                t1 = 1.0F;
            }

        } else {
            // N dot D is not 0. Calculate intersection interval:
            t0 = (-1.0F - signedDistToTrianglePlane) / normalDotVelocity;
            t1 = (+1.0F - signedDistToTrianglePlane) / normalDotVelocity;
            // Swap so t0 < t1
            if (t0 > t1) {
                float temp = t1;
                t1 = t0;
                t0 = temp;
            }
            // Check that at least one result is within range:
            if (t0 > 1.0f || t1 < 0.0f) {
                // Both t values are outside values [0,1]
                // No collision possible:
                return;
            }
            // Clamp to [0,1]
            t0 = Maths.clamp(t0, 0, 1);
            t1 = Maths.clamp(t1, 0, 1);
        }
        // OK, at this point we have two time values t0 and t1
        // between which the swept sphere intersects with the
        // triangle plane. If any collision is to occur it must
        // happen within this interval.
        Vector3f collisionPoint = null;
        boolean foundCollision = false;
        float t = 1.0F;
        // First we check for the easy case - collision inside
        // the triangle. If this happens it must be at time t0
        // as this is when the sphere rests on the front side
        // of the triangle plane. Note, this can only happen if
        // the sphere is not embedded in the triangle plane.
        if (!embeddedInPlane) {
            Vector3f planeIntersectionPoint = Vector3
                    .mul(velocity, t0)
                    .add(basePoint)
                    .sub(trianglePlane.getNormal());
            if (triangle.contains(planeIntersectionPoint)) {
                collisionPoint = planeIntersectionPoint;
                foundCollision = true;
                t = t0;
            }
        }
        // if we haven’t found a collision already we’ll have to
        // sweep sphere against points and edges of the triangle.
        // Note: A collision inside the triangle (the check above)
        // will always happen before a vertex or edge collision!
        // This is why we can skip the swept test if the above
        // gives a collision!
        if (!foundCollision) {
            // some commonly used terms:
            Vector3f base = Vector3.of(basePoint);
            float velocitySquaredLength = velocity.lengthSquared();
            float a, b, c; // Params for equation
            float newT;
            // For each vertex or edge a quadratic equation have to
            // be solved. We parameterize this equation as
            // a*t^2 + b*t + c = 0 and below we calculate the
            // parameters a,b and c for each test.
            // Check against points:
            a = velocitySquaredLength;
            /*
                Pointer<Float> tPointer = Pointer.nullptr();
                Pointer<Boolean> fcPointer = Pointer.nullptr();
                Pointer<Vector3f> cpPointer = Pointer.nullptr();

                edge(velocity, base, p1, a, tPointer, fcPointer, cpPointer);
                edge(velocity, base, p2, a, tPointer, fcPointer, cpPointer);
                edge(velocity, base, p3, a, tPointer, fcPointer, cpPointer);
            */
            // P1
            b = 2.0F * (velocity.dot(Vector3.sub(base, p1)));
            c = Vector3.sub(p1, base).lengthSquared() - 1;
            newT = getPositiveRootBelow(a, b, c, t);
            if (!Float.isNaN(newT)) {
                t = newT;
                foundCollision = true;
                collisionPoint = Vector3.of(p1);
            }

            // P2
            b = 2.0F * (velocity.dot(Vector3.sub(base, p2)));
            c = Vector3.sub(p2, base).lengthSquared() - 1;
            newT = getPositiveRootBelow(a, b, c, t);
            if (!Float.isNaN(newT)) {
                t = newT;
                foundCollision = true;
                collisionPoint = Vector3.of(p2);
            }

            // P3
            b = 2.0F * velocity.dot(Vector3.sub(base, p3));
            c = Vector3.sub(p3, base).lengthSquared() - 1.0F;
            newT = getPositiveRootBelow(a, b, c, t);
            if (!Float.isNaN(newT)) {
                t = newT;
                foundCollision = true;
                collisionPoint = Vector3.of(p3);
            }

            // Check against edges:
            // p1.p2:
            Vector3f edge = Vector3.sub(p2, p1);
            Vector3f baseToVertex = Vector3.sub(p1, base);
            float edgeSquaredLength = edge.lengthSquared();
            float edgeDotVelocity = edge.dot(velocity);
            float edgeDotBaseToVertex = edge.dot(baseToVertex);
            // Calculate parameters for equation
            a = edgeSquaredLength * -velocitySquaredLength + edgeDotVelocity * edgeDotVelocity;
            b = edgeSquaredLength * (2 * velocity.dot(baseToVertex)) - 2 * edgeDotVelocity * edgeDotBaseToVertex;
            c = edgeSquaredLength * (1 - baseToVertex.lengthSquared()) + edgeDotBaseToVertex * edgeDotBaseToVertex;
            // Does the swept sphere collide against infinite edge?
            newT = getPositiveRootBelow(a, b, c, t);
            if (!Float.isNaN(newT)) {
                // Check if intersection is within line segment:
                float f = (edgeDotVelocity * newT - edgeDotBaseToVertex) /
                        edgeSquaredLength;
                if (f >= 0.0 && f <= 1.0) {
                    // intersection took place within segment.
                    t = newT;
                    foundCollision = true;
                    collisionPoint = Vector3.mul(edge, f).add(p1);
                }
            }

            // p2.p3:
            edge = Vector3.sub(p3, p2);
            baseToVertex = Vector3.sub(p2, base);
            edgeSquaredLength = edge.lengthSquared();
            edgeDotVelocity = edge.dot(velocity);
            edgeDotBaseToVertex = edge.dot(baseToVertex);
            a = edgeSquaredLength * -velocitySquaredLength +
                    edgeDotVelocity * edgeDotVelocity;
            b = edgeSquaredLength * (2 * velocity.dot(baseToVertex)) -
                    2.0F * edgeDotVelocity * edgeDotBaseToVertex;
            c = edgeSquaredLength * (1 - baseToVertex.lengthSquared()) +
                    edgeDotBaseToVertex * edgeDotBaseToVertex;
            newT = getPositiveRootBelow(a, b, c, t);
            if (!Float.isNaN(newT)) {
                float f = (edgeDotVelocity * newT - edgeDotBaseToVertex) /
                        edgeSquaredLength;
                if (f >= 0.0 && f <= 1.0) {
                    t = newT;
                    foundCollision = true;
                    collisionPoint = Vector3.mul(edge, f).add(p2);
                }
            }

            // p3.p1:
            edge = Vector3.sub(p1, p3);
            baseToVertex = Vector3.sub(p3, base);
            edgeSquaredLength = edge.lengthSquared();
            edgeDotVelocity = edge.dot(velocity);
            edgeDotBaseToVertex = edge.dot(baseToVertex);
            a = edgeSquaredLength * -velocitySquaredLength +
                    edgeDotVelocity * edgeDotVelocity;
            b = edgeSquaredLength * (2 * velocity.dot(baseToVertex)) -
                    2.0F * edgeDotVelocity * edgeDotBaseToVertex;
            c = edgeSquaredLength * (1 - baseToVertex.lengthSquared()) +
                    edgeDotBaseToVertex * edgeDotBaseToVertex;
            newT = getPositiveRootBelow(a, b, c, t);
            if (!Float.isNaN(newT)) {
                float f = (edgeDotVelocity * newT - edgeDotBaseToVertex) /
                        edgeSquaredLength;
                if (f >= 0.0 && f <= 1.0) {
                    t = newT;
                    foundCollision = true;
                    collisionPoint = Vector3.mul(edge, f).add(p3);
                }
            }
        }
        // Set result:
        if (foundCollision) {
            // distance to collision: ’t’ is time of collision
            float distToCollision = t * velocity.length();
            // Does this triangle qualify for the closest hit?
            // it does if it’s the first hit or the closest
            if (collisionEvent.isCollisionFound() ||
                    distToCollision < collisionEvent.getNearestDistance()) {
                // Collision information necessary for sliding
                collisionEvent.setNearestDistance(distToCollision);
                collisionEvent.setIntersectionPoint(collisionPoint.x, collisionPoint.y, collisionPoint.z);
                collisionEvent.foundCollision();
            }
        }
    }

    private static float getPositiveRootBelow(float a, float b, float c, float max) {
        float sqrt = b * b - 4 * a * c;
        if (sqrt < 0) return Float.NaN;
        sqrt = (float) Math.sqrt(sqrt);

        float x1 = (-b - sqrt) / (2 * a);
        float x2 = (-b + sqrt) / (2 * a);

        if (Maths.isBetween(x1, 0f, max)) return x1;
        if (Maths.isBetween(x2, 0f, max)) return x2;

        return Float.NaN;
    }

    private static void edge(Vector3fc velocity, Vector3fc base, Vector3fc p, float a,
                             Pointer<Float> tPointer, Pointer<Boolean> fcPointer, Pointer<Vector3f> cpPointer) {
        float b = 2.0F * (velocity.dot(Vector3.sub(base, p)));
        float c = Vector3.sub(p, base).lengthSquared() - 1;
        float newT = getPositiveRootBelow(a, b, c, tPointer.value);
        if (!Float.isNaN(newT)) {
            tPointer.value = newT;
            fcPointer.value = true;
            cpPointer.value = Vector3.of(p);
        }
    }

}

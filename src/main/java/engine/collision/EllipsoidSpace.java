package engine.collision;

import maths.objects.Ellipsoid;

import java.util.ArrayList;
import java.util.List;

public class EllipsoidSpace {

    private final Ellipsoid ellipsoid;
    private final TriangleBatch triangles;

    public EllipsoidSpace(Ellipsoid ellipsoid, TriangleBatch triangles) {
        this.ellipsoid = ellipsoid.toSpace(ellipsoid.getDimensions());
        this.triangles = triangles.toSpace(ellipsoid.getDimensions());
    }

    public Ellipsoid getEllipsoid() {
        return ellipsoid;
    }

    public TriangleBatch getTriangles() {
        return triangles;
    }
}

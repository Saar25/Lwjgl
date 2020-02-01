package engine.collision;

import maths.objects.Ellipsoid;
import maths.objects.Triangle;

import java.util.ArrayList;
import java.util.List;

public class CollisionModule {

    private final TriangleBatch triangles;
    private final List<EllipsoidSpace> ellipsoids;

    public CollisionModule() {
        this.triangles = new TriangleBatch();
        this.ellipsoids = new ArrayList<>();
    }

    public void addTriangle(Triangle triangle) {
        triangles.insert(triangle);
    }

    public void addEllipsoid(Ellipsoid ellipsoid) {
        ellipsoids.add(new EllipsoidSpace(ellipsoid, triangles));
    }

    public void removeTriangle(Triangle triangle) {
        triangles.remove(triangle);
    }

    public void removeEllipsoid(Ellipsoid ellipsoid) {
        ellipsoids.removeIf(eSpace -> eSpace.getEllipsoid() == ellipsoid);
    }

}

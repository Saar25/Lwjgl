package engine.collision;

import maths.objects.Triangle;
import maths.joml.Vector3fc;

import java.util.ArrayList;
import java.util.List;

public class TriangleBatch {

    private final List<Triangle> triangles;

    public TriangleBatch() {
        this.triangles = new ArrayList<>();
    }

    public void insert(Triangle triangle) {
        this.triangles.add(triangle);
    }

    public void remove(Triangle triangle) {
        this.triangles.remove(triangle);
    }

    public TriangleBatch toSpace(Vector3fc space) {
        TriangleBatch triangleBatch = new TriangleBatch();
        for (Triangle triangle : triangles) {
            triangleBatch.insert(triangle.toSpace(space));
        }
        return triangleBatch;
    }

    public List<Triangle> getTriangles() {
        return triangles;
    }
}

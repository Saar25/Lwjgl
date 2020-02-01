package maths.objects;

import maths.joml.Vector2f;
import maths.utils.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Polygon {

    private final List<Vector2f> vertices = new ArrayList<>();

    public void addVertex(float x, float y) {
        vertices.add(Vector2.of(x, y));
    }

    public List<Vector2f> getVertices() {
        return vertices;
    }
}

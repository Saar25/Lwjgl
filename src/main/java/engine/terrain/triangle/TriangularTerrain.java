package engine.terrain.triangle;

import engine.models.Model;
import engine.models.ModelGenerator;
import engine.models.Skin;
import engine.terrain.Terrain;
import maths.joml.Matrix4f;
import maths.joml.Vector4f;
import maths.objects.Triangle;
import maths.utils.Vector2;
import maths.utils.Vector3;
import maths.utils.Vector4;

public class TriangularTerrain extends Terrain {

    private static final Model MODEL = ModelGenerator.generateTriangle();

    private final Triangle triangle;

    public TriangularTerrain(Triangle triangle, float size) {
        super(MODEL, Skin.create(), size);
        this.triangle = triangle;
    }

    @Override
    public float getHeight(float x, float z) {
        return transformTriangle().getHeight(Vector2.of(x, z));
    }

    @Override
    public boolean isInside(float x, float z) {
        return transformTriangle().contains(x, z);
    }

    @Override
    public void process() {
        TriangularTerrainRenderer.getInstance().process(this);
    }

    private Triangle transformTriangle() {
        Matrix4f transformation = getTransform().getTransformationMatrix();
        Vector4f p1 = Vector4.of(triangle.getP1(), 1).mul(transformation);
        Vector4f p2 = Vector4.of(triangle.getP2(), 1).mul(transformation);
        Vector4f p3 = Vector4.of(triangle.getP3(), 1).mul(transformation);
        return new Triangle(Vector3.of(p1).div(p1.w), Vector3.of(p2).div(p2.w), Vector3.of(p3).div(p3.w));
    }
}

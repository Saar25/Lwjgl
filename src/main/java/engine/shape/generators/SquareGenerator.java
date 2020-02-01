package engine.shape.generators;

import engine.models.Model;
import engine.models.SimpleModel;
import maths.joml.Vector3f;
import maths.objects.Triangle;
import maths.utils.Vector3;
import opengl.constants.DataType;
import opengl.constants.RenderMode;
import opengl.constants.VboUsage;
import opengl.objects.*;

import java.util.ArrayList;
import java.util.List;

public class SquareGenerator implements IModelGenerator {

    private static final SquareGenerator instance = new SquareGenerator();

    private static final float[] positions = {
            -0.5f, -0.5f, // 0
            -0.5f, +0.5f, // 1
            +0.5f, -0.5f, // 2
            +0.5f, +0.5f, // 3
    };
    private static final int[] indices = {
            0, 1, 2, 2, 3, 0
    };

    public static SquareGenerator getInstance() {
        return SquareGenerator.instance;
    }


    @Override
    public Vao createVao() {
        final Vao vao = Vao.create();
        vao.loadDataBuffer(createDataVbo(), Attribute.of(0, 2, DataType.FLOAT, false));
        return vao;
    }

    @Override
    public DataBuffer createDataVbo() {
        return BufferUtils.loadToDataBuffer(VboUsage.STATIC_DRAW, positions);
    }

    @Override
    public IndexBuffer createIndexVbo() {
        return BufferUtils.loadToIndexBuffer(VboUsage.STATIC_DRAW, indices);
    }

    @Override
    public Model generateModel() {
        return new SimpleModel(Vao.create(), RenderMode.TRIANGLE_STRIP);
    }

    private static List<Triangle> createTriangles() {
        final List<Triangle> triangles = new ArrayList<>();
        for (int i = 0; i < indices.length; i += 3) {
            Vector3f v1 = getVertex(indices[i]);
            Vector3f v2 = getVertex(indices[i + 1]);
            Vector3f v3 = getVertex(indices[i + 2]);
            triangles.add(new Triangle(v1, v2, v3));
        }
        return triangles;
    }

    private static Vector3f getVertex(int i) {
        return Vector3.of(positions[i], positions[i + 1], positions[i + 2]);
    }
}

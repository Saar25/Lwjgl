package engine.shape.generators;

import engine.models.Model;
import engine.models.SimpleModel;
import engine.util.Lazy;
import maths.joml.Vector3f;
import maths.objects.Box;
import maths.objects.Triangle;
import maths.utils.Vector3;
import opengl.constants.DataType;
import opengl.constants.RenderMode;
import opengl.constants.VboUsage;
import opengl.objects.*;

import java.util.ArrayList;
import java.util.List;

public class CubeGenerator implements IModelGenerator {

    public static final CubeGenerator instance = new CubeGenerator();

    private final Box bounds = new Box(Vector3.of(-.5f), Vector3.of(.5f));
    private final Lazy<Model> MODEL = new Lazy<>(() ->
            new SimpleModel(createVao(), RenderMode.TRIANGLES, bounds));

    private static final float[] positions = {
            -0.5f, -0.5f, -0.5f, // 0
            -0.5f, +0.5f, -0.5f, // 1
            +0.5f, +0.5f, -0.5f, // 2
            +0.5f, -0.5f, -0.5f, // 3
            -0.5f, -0.5f, +0.5f, // 4
            -0.5f, +0.5f, +0.5f, // 5
            +0.5f, +0.5f, +0.5f, // 6
            +0.5f, -0.5f, +0.5f, // 7
    };

    private static float[] flatData = new float[]{ // xyz position, xyz normal,
            -0.5f, -0.5f, -0.5f, 0, 0, -1, // 0
            -0.5f, +0.5f, -0.5f, 0, +1, 0, // 1
            +0.5f, +0.5f, -0.5f, +1, 0, 0, // 2
            +0.5f, -0.5f, -0.5f, 0, -1, 0, // 3
            -0.5f, -0.5f, +0.5f, -1, 0, 0, // 4
            -0.5f, +0.5f, +0.5f, 0, 0f, 0, // 5
            +0.5f, +0.5f, +0.5f, 0, 0f, 0, // 6
            +0.5f, -0.5f, +0.5f, 0, 0, +1, // 7
    };

    private static final float len = (float) Math.sqrt(.5 * .5 * 3);
    private static float[] smoothData = new float[]{ // xyz position, xyz normal,
            -0.5f, -0.5f, -0.5f, -0.5f / len, -0.5f / len, -0.5f / len, // 0
            -0.5f, +0.5f, -0.5f, -0.5f / len, +0.5f / len, -0.5f / len, // 1
            +0.5f, +0.5f, -0.5f, +0.5f / len, +0.5f / len, -0.5f / len, // 2
            +0.5f, -0.5f, -0.5f, +0.5f / len, -0.5f / len, -0.5f / len, // 3
            -0.5f, -0.5f, +0.5f, -0.5f / len, -0.5f / len, +0.5f / len, // 4
            -0.5f, +0.5f, +0.5f, -0.5f / len, +0.5f / len, +0.5f / len, // 5
            +0.5f, +0.5f, +0.5f, +0.5f / len, +0.5f / len, +0.5f / len, // 6
            +0.5f, -0.5f, +0.5f, +0.5f / len, -0.5f / len, +0.5f / len, // 7
    };

    private static float[] smoothData2 = new float[]{ // xyz position, xyz normal,
            -0.5f, -0.5f, -0.5f, +0.0f, +0.0f, -1.0f, // 0
            -0.5f, +0.5f, -0.5f, +0.0f, +1.0f, +0.0f, // 1
            +0.5f, +0.5f, -0.5f, +1.0f, +0.0f, +0.0f, // 2
            +0.5f, -0.5f, -0.5f, +0.0f, -1.0f, +0.0f, // 3
            -0.5f, -0.5f, +0.5f, -1.0f, +0.0f, +0.0f, // 4
            -0.5f, +0.5f, +0.5f, +0.0f, +0.0f, +0.0f, // 5
            +0.5f, +0.5f, +0.5f, +0.0f, +0.0f, +0.0f, // 6
            +0.5f, -0.5f, +0.5f, +0.0f, +0.0f, +1.0f, // 7
    };

    private static final int[] indices = {
            0, 1, 2, 0, 2, 3, // back   , PV: 0
            4, 5, 1, 4, 1, 0, // left   , PV: 4
            7, 6, 5, 7, 5, 4, // front  , PV: 7
            2, 6, 7, 2, 7, 3, // right  , PV: 2
            1, 5, 6, 1, 6, 2, // top    , PV: 1
            3, 7, 4, 3, 4, 0, // bottom , PV: 3
    };

    public static CubeGenerator getInstance() {
        return CubeGenerator.instance;
    }

    @Override
    public Vao createVao() {
        final Vao vao = Vao.create();
        vao.loadIndexBuffer(createIndexVbo());
        vao.loadDataBuffer(createDataVbo(), Attribute.ofPositions(),
                Attribute.of(1, 3, DataType.FLOAT, false));
        vao.unbind();
        return vao;
    }

    @Override
    public DataBuffer createDataVbo() {
        return BufferUtils.loadToDataBuffer(VboUsage.STATIC_DRAW, smoothData2);
    }

    @Override
    public IndexBuffer createIndexVbo() {
        return BufferUtils.loadToIndexBuffer(VboUsage.STATIC_DRAW, indices);
    }

    @Override
    public Model generateModel() {
        return MODEL.get();
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

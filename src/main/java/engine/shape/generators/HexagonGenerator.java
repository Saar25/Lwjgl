package engine.shape.generators;

import engine.models.Model;
import engine.models.SimpleModel;
import maths.utils.Maths;
import opengl.constants.RenderMode;
import opengl.constants.VboUsage;
import opengl.objects.BufferUtils;
import opengl.objects.DataBuffer;
import opengl.objects.IndexBuffer;
import opengl.objects.Vao;

public class HexagonGenerator implements IModelGenerator {

    private final float[] data = new float[]{
            0, 0, 0,
            -1, 0, 0,
            -.5f, 0, Maths.sqrt(3) / 2,
            .5f, 0, Maths.sqrt(3) / 2,
            1, 0, 0,
            .5f, 0, -Maths.sqrt(3) / 2,
            -.5f, 0, -Maths.sqrt(3) / 2,
    };

    private final int[] indices = new int[]{
            0, 1, 2,
            0, 2, 3,
            0, 3, 4,
            0, 4, 5,
            0, 5, 6,
            0, 6, 7,
            0, 8, 9,
            0, 9, 1,
    };

    @Override
    public Vao createVao() {
        final Vao vao = Vao.create();
        vao.loadIndexBuffer(createIndexVbo());
        vao.loadDataBuffer(createDataVbo());
        return vao;
    }

    @Override
    public DataBuffer createDataVbo() {
        return BufferUtils.loadToDataBuffer(VboUsage.STATIC_DRAW, data);
    }

    @Override
    public IndexBuffer createIndexVbo() {
        return BufferUtils.loadToIndexBuffer(VboUsage.STATIC_DRAW, indices);
    }

    @Override
    public Model generateModel() {
        return new SimpleModel(createVao(), RenderMode.TRIANGLE_FAN);
    }
}

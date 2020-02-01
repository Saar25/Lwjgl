package engine.shape.generators;

import engine.models.Model;
import engine.models.SimpleModel;
import opengl.constants.DataType;
import opengl.constants.RenderMode;
import opengl.constants.VboUsage;
import opengl.objects.*;

import java.util.Arrays;

public class PlaneGenerator implements IModelGenerator {

    private static final PlaneGenerator instance = new PlaneGenerator(16);

    private final int vertices;

    public PlaneGenerator(int vertices) {
        this.vertices = Math.max(vertices, 2);
    }

    public static PlaneGenerator getInstance() {
        return PlaneGenerator.instance;
    }

    @Override
    public Vao createVao() {
        final Vao vao = Vao.create();
        vao.loadIndexBuffer(createIndexVbo());
        vao.loadDataBuffer(createDataVbo(), Attribute.of(0, 2, DataType.FLOAT, false));
        return vao;
    }

    @Override
    public DataBuffer createDataVbo() {
        final float[] positions = new float[vertices * vertices * 2];

        int pointer = 0;
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                positions[pointer++] = j / (vertices - 1f);
                positions[pointer++] = i / (vertices - 1f);
            }
        }

        return BufferUtils.loadToDataBuffer(VboUsage.STATIC_DRAW, positions);
    }

    @Override
    public IndexBuffer createIndexVbo() {
        return createIndexVbo(0);
    }

    @Override
    public IndexBuffer createIndexVbo(int lod) {
        final int indicesCount = 6 * (vertices - 1) * (vertices - 1);
        final int[] indices = new int[indicesCount];

        int pointer = 0;
        final int inc = lod + 1;
        for (int z = 0; z < vertices - 1; z += inc) {
            for (int x = 0; x < vertices - 1; x += inc) {
                int incX = Math.min(vertices - x - 1, inc);
                int incZ = Math.min(vertices - z - 1, inc);
                final int[] corners = {
                        (z * vertices) + x, ((z + incZ) * vertices) + x + incX,
                        (z * vertices) + x + incX, ((z + incZ) * vertices) + x};

                indices[pointer++] = corners[0]; // Top Left
                indices[pointer++] = corners[3]; // Bottom Left
                indices[pointer++] = corners[2]; // Top Right

                indices[pointer++] = corners[2]; // Top Right
                indices[pointer++] = corners[3]; // Bottom Left
                indices[pointer++] = corners[1]; // Bottom Right
            }
        }

        return BufferUtils.loadToIndexBuffer(VboUsage.STATIC_DRAW, Arrays.copyOf(indices, pointer));
    }

    @Override
    public Model generateModel() {
        return new SimpleModel(createVao(), RenderMode.TRIANGLES);
    }
}

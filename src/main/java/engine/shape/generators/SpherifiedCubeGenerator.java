package engine.shape.generators;

import engine.models.Model;
import engine.models.SimpleModel;
import maths.joml.Vector3f;
import maths.utils.Vector3;
import opengl.constants.DataType;
import opengl.constants.RenderMode;
import opengl.constants.VboUsage;
import opengl.objects.*;
import opengl.utils.MemoryUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class SpherifiedCubeGenerator implements IModelGenerator {

    private static final SpherifiedCubeGenerator instance = new SpherifiedCubeGenerator(16);

    private final int vertices;

    public SpherifiedCubeGenerator(int vertices) {
        this.vertices = vertices;
    }

    public static SpherifiedCubeGenerator getInstance() {
        return instance;
    }

    @Override
    public Vao createVao() {
        final Vao vao = Vao.create();
        vao.loadIndexBuffer(createIndexVbo());
        vao.loadDataBuffer(createDataVbo(), Attribute.ofPositions());
        vao.loadDataBuffer(createDataVbo(), Attribute.of(1, 3, DataType.FLOAT, false));
        vao.unbind();
        return vao;
    }

    @Override
    public DataBuffer createDataVbo() {
        final FloatBuffer buffer = MemoryUtils.allocFloat(vertices * vertices * 6 * 3);

        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                buffer.put(i / (vertices - 1f) - .5f);
                buffer.put(j / (vertices - 1f) - .5f);
                buffer.put(+.5f);
            }
        }

        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                buffer.put(j / (vertices - 1f) - .5f);
                buffer.put(i / (vertices - 1f) - .5f);
                buffer.put(-.5f);
            }
        }

        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                buffer.put(j / (vertices - 1f) - .5f);
                buffer.put(+.5f);
                buffer.put(i / (vertices - 1f) - .5f);
            }
        }

        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                buffer.put(i / (vertices - 1f) - .5f);
                buffer.put(-.5f);
                buffer.put(j / (vertices - 1f) - .5f);
            }
        }

        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                buffer.put(+.5f);
                buffer.put(i / (vertices - 1f) - .5f);
                buffer.put(j / (vertices - 1f) - .5f);
            }
        }

        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                buffer.put(-.5f);
                buffer.put(j / (vertices - 1f) - .5f);
                buffer.put(i / (vertices - 1f) - .5f);
            }
        }
        buffer.flip();
        for (int i = 0; i < buffer.limit(); i += 3) {
            float x = buffer.get(i);
            float y = buffer.get(i + 1);
            float z = buffer.get(i + 2);

            final Vector3f v = Vector3.of(x, y, z).normalize();
            buffer.put(v.x).put(v.y).put(v.z);
        }
        buffer.flip();

        return BufferUtils.loadToDataBuffer(VboUsage.STATIC_DRAW, buffer);
    }

    @Override
    public IndexBuffer createIndexVbo() {
        return createIndexVbo(0);
    }

    @Override
    public IndexBuffer createIndexVbo(int lod) {
        final int indicesCount = 6 * 6 * (vertices - 1) * (vertices - 1);
        final IntBuffer buffer = MemoryUtils.allocInt(indicesCount);

        final int inc = lod + 1;
        for (int i = 0; i < 6; i++) {
            for (int z = 0; z < vertices - 1; z += inc) {
                for (int x = 0; x < vertices - 1; x += inc) {
                    int incX = Math.min(vertices - x - 1, inc);
                    int incZ = Math.min(vertices - z - 1, inc);
                    final int[] corners = {
                            (z * vertices) + x, ((z + incZ) * vertices) + x + incX,
                            (z * vertices) + x + incX, ((z + incZ) * vertices) + x};

                    final int start = vertices * vertices * i;

                    buffer.put(start + corners[0]); // Top Left
                    buffer.put(start + corners[3]); // Bottom Left
                    buffer.put(start + corners[2]); // Top Right

                    buffer.put(start + corners[2]); // Top Right
                    buffer.put(start + corners[3]); // Bottom Left
                    buffer.put(start + corners[1]); // Bottom Right
                }
            }
        }
        buffer.flip();

        return BufferUtils.loadToIndexBuffer(VboUsage.STATIC_DRAW, buffer);
    }

    @Override
    public Model generateModel() {
        return new SimpleModel(createVao(), RenderMode.TRIANGLES);
    }
}

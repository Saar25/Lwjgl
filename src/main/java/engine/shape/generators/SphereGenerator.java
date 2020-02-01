package engine.shape.generators;

import engine.models.Model;
import engine.models.SimpleModel;
import maths.joml.Vector3f;
import maths.noise.Noise3f;
import maths.utils.Maths;
import maths.utils.Vector3;
import opengl.constants.DataType;
import opengl.constants.RenderMode;
import opengl.constants.VboUsage;
import opengl.objects.*;
import opengl.utils.MemoryUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class SphereGenerator implements IModelGenerator {

    private static final SphereGenerator instance = new SphereGenerator(15, 15);

    private final int stacks;
    private final int sectors;
    private final Noise3f noise;

    public SphereGenerator(int stacks, int sectors) {
        this.stacks = stacks;
        this.sectors = sectors;
        this.noise = Noise3f.ONE;
    }

    public SphereGenerator(int stacks, int sectors, Noise3f noise) {
        this.stacks = stacks;
        this.sectors = sectors;
        this.noise = noise;
    }

    public static SphereGenerator getInstance() {
        return instance;
    }

    @Override
    public Vao createVao() {
        final Vao vao = Vao.create();
        vao.loadIndexBuffer(createIndexVbo());
        final DataBuffer dataBuffer = createDataVbo();
        vao.loadDataBuffer(dataBuffer, Attribute.ofPositions());
        vao.loadDataBuffer(dataBuffer, Attribute.of(1, 3, DataType.FLOAT, false));
        vao.unbind();
        return vao;
    }

    @Override
    public DataBuffer createDataVbo() {
        final int size = (stacks + 1) * (sectors + 1) * 3;
        final FloatBuffer buffer = MemoryUtils.allocFloat(size);

        final float sectorStep = 2 * Maths.PI / sectors;
        final float stackStep = Maths.PI / stacks;

        for (int i = 0; i <= stacks; i++) {
            final float stackAngle = Maths.PI / 2 - i * stackStep;
            final float xy = Maths.cosf(stackAngle);
            final float z = Maths.sinf(stackAngle);

            for (int j = 0; j <= sectors; j++) {
                final float sectorAngle = j * sectorStep;
                final float x = xy * Maths.cosf(sectorAngle);
                final float y = xy * Maths.sinf(sectorAngle);
                final Vector3f position = Vector3.of(x, y, z);
                final float r = noise.noise(x, y, z);
                position.normalize(r);
                buffer.put(position.x).put(position.y).put(position.z);
            }
        }
        buffer.flip();

        return BufferUtils.loadToDataBuffer(VboUsage.STATIC_DRAW, buffer);
    }

    @Override
    public IndexBuffer createIndexVbo() {
        final IntBuffer buffer = MemoryUtils.allocInt(stacks * (sectors - 1) * 6);

        for (int i = 0; i < stacks; i++) {
            int k1 = i * (sectors + 1);
            int k2 = k1 + sectors + 1;
            for (int j = 0; j < sectors; j++, k1++, k2++) {
                if (i != 0) {
                    buffer.put(k2);
                    buffer.put(k1 + 1);
                    buffer.put(k1);
                }
                if (i != (stacks - 1)) {
                    buffer.put(k2);
                    buffer.put(k2 + 1);
                    buffer.put(k1 + 1);
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

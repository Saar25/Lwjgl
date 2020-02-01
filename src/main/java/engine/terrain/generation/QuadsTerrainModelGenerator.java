package engine.terrain.generation;

import engine.models.Model;
import engine.models.SimpleModel;
import engine.shape.generators.IModelGenerator;
import maths.joml.Vector3f;
import maths.joml.Vector3fc;
import maths.utils.Maths;
import maths.utils.Vector3;
import opengl.constants.DataType;
import opengl.constants.RenderMode;
import opengl.constants.VboUsage;
import opengl.objects.*;
import opengl.utils.MemoryUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class QuadsTerrainModelGenerator implements TerrainModelGenerator, IModelGenerator {

    private static final Vector3f v1 = Vector3.create();
    private static final Vector3f v2 = Vector3.create();
    private static final Vector3f v3 = Vector3.create();

    private final int vertexCount;
    private final IndexBuffer[] levels;
    private final float space;

    private final HeightGenerator heightGenerator;

    private final Vector3f position = Vector3.create();
    private float terrainSize;

    public QuadsTerrainModelGenerator(int vertexCount, HeightGenerator heightGenerator, int... levels) {
        this.vertexCount = vertexCount;
        this.space = 1f / (vertexCount - 1);
        this.heightGenerator = heightGenerator;

        this.levels = levels.length > 0 ? new IndexBuffer[levels.length]
                : new IndexBuffer[]{createIndexVbo()};
        for (int i = 0; i < levels.length; i++) {
            this.levels[i] = createIndexVbo(levels[i]);
        }
    }

    @Override
    public Vao createVao() {
        final Vao vao = Vao.create();
        vao.loadIndexBuffer(levels[0]);
        vao.loadDataBuffer(createDataVbo(),
                Attribute.of(0, 3, DataType.FLOAT, false),  // position
                Attribute.of(1, 2, DataType.FLOAT, false),  // texCoords
                Attribute.of(2, 3, DataType.FLOAT, false)); // normal
        return vao;
    }

    @Override
    public DataBuffer createDataVbo() {
        final FloatBuffer dataBuffer = MemoryUtils.allocFloat(vertexCount * vertexCount * 8);

        for (int x = 0; x < vertexCount; x++) {
            final float vx = x * space - .5f;
            for (int z = 0; z < vertexCount; z++) {
                final float vz = z * space - .5f;
                putVertex(dataBuffer, vx, vz);
            }
        }
        dataBuffer.flip();

        return BufferUtils.loadToDataBuffer(VboUsage.STATIC_DRAW, dataBuffer);
    }

    private void putVertex(FloatBuffer dataBuffer, float x, float z) {
        final float tSpace = space * terrainSize;
        final float tx = position.x + x * terrainSize;
        final float tz = position.z + z * terrainSize;
        final float height = heightGenerator.getHeight(tx, tz);

        final Vector3f normal = Maths.calculateNormal(v1.set(tx, height, tz),
                v2.set(tx, heightGenerator.getHeight(tx, tz - tSpace), tz - tSpace),
                v3.set(tx - tSpace, heightGenerator.getHeight(tx - tSpace, tz), tz));

        dataBuffer.put(x * terrainSize).put(height).put(z * terrainSize);
        dataBuffer.put(x).put(z);
        dataBuffer.put(normal.x).put(normal.y).put(normal.z);
    }

    @Override
    public IndexBuffer createIndexVbo() {
        return createIndexVbo(0);
    }

    @Override
    public Model generateModel() {
        return new SimpleModel(Vao.create(), RenderMode.TRIANGLES, levels);
    }

    @Override
    public IndexBuffer createIndexVbo(int lod) {
        if (lod < 0) {
            return IndexBuffer.NULL;
        }
        final IntBuffer indexBuffer = MemoryUtils.allocInt((vertexCount - 1) * (vertexCount - 1) * 4 * 3);

        int inc = lod + 1;
        for (int x = 0; x < vertexCount - 1; x += inc) {
            for (int z = 0; z < vertexCount - 1; z += inc) {
                final int incX = Math.min(vertexCount - x - 1, inc);
                final int incZ = Math.min(vertexCount - z - 1, inc);

                final int[] corners = new int[]{
                        (z * vertexCount) + x, ((z + incZ) * vertexCount) + x + incX,
                        (z * vertexCount) + x + incX, ((z + incZ) * vertexCount) + x};

                indexBuffer.put(corners[0]);
                indexBuffer.put(corners[2]);
                indexBuffer.put(corners[3]);

                indexBuffer.put(corners[2]);
                indexBuffer.put(corners[1]);
                indexBuffer.put(corners[3]);
            }
        }
        indexBuffer.flip();

        return BufferUtils.loadToIndexBuffer(VboUsage.STATIC_DRAW, indexBuffer);
    }

    @Override
    public Model generateModel(Vector3fc position, float size) {
        this.terrainSize = size;
        this.position.set(position);
        return new SimpleModel(createVao(), RenderMode.TRIANGLES, levels);
    }

}

package engine.terrain.generation;

import engine.models.Model;
import engine.models.SimpleModel;
import maths.joml.Vector3f;
import maths.joml.Vector3fc;
import maths.utils.Maths;
import maths.utils.Vector3;
import opengl.constants.DataType;
import opengl.constants.RenderMode;
import opengl.constants.VboUsage;
import opengl.objects.*;
import opengl.utils.MemoryUtils;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleTerrainModelGenerator implements TerrainModelGenerator {

    private static final Map<Integer, Map<Integer, IndexBuffer>> indexBuffers = new HashMap<>();

    private static final Vector3f v1 = Vector3.create();
    private static final Vector3f v2 = Vector3.create();
    private static final Vector3f v3 = Vector3.create();

    private final int vertexCount;
    private final HeightGenerator generator;
    private final float[][] heights;

    public SimpleTerrainModelGenerator(int vertexCount, HeightGenerator generator, float[][] heights) {
        this.vertexCount = vertexCount;
        this.generator = generator;
        this.heights = heights;
    }

    public SimpleTerrainModelGenerator(int vertexCount, HeightGenerator generator) {
        this.vertexCount = vertexCount;
        this.generator = generator;
        this.heights = new float[vertexCount][vertexCount];
    }

    private IndexBuffer getIndexBuffer(int level) {
        if (level < 0) {
            return IndexBuffer.NULL;
        }
        final Map<Integer, IndexBuffer> map = indexBuffers.computeIfAbsent(vertexCount, k -> new HashMap<>());

        IndexBuffer indexBuffer = map.get(Math.max(vertexCount - 3, level));
        if (indexBuffer != null) {
            return indexBuffer;
        }

        int indicesSize = 6 * (vertexCount - 1) * (vertexCount - 1);
        final IntBuffer indices = MemoryUtils.allocInt(indicesSize);

        final int inc = level + 1;
        for (int z = 0; z < vertexCount - 1; z += inc) {
            for (int x = 0; x < vertexCount - 1; x += inc) {
                int incX = Math.min(vertexCount - x - 1, inc);
                int incZ = Math.min(vertexCount - z - 1, inc);

                final int[] corners = new int[]{
                        (z * vertexCount) + x, ((z + incZ) * vertexCount) + x + incX,
                        (z * vertexCount) + x + incX, ((z + incZ) * vertexCount) + x};

                indices.put(corners[0]);
                indices.put(corners[2]);
                indices.put(corners[3]);

                indices.put(corners[2]);
                indices.put(corners[1]);
                indices.put(corners[3]);
            }
        }

        indexBuffer = BufferUtils.loadToIndexBuffer(VboUsage.STATIC_DRAW, indices);

        map.put(level, indexBuffer);

        return indexBuffer;
    }

    @Override
    public Model generateModel(Vector3fc position, float size) {
        int modelDataSize = 8 * vertexCount * vertexCount;

        final Vao vao = Vao.create();
        final DataBuffer dataBuffer = new DataBuffer(VboUsage.STATIC_DRAW);
        dataBuffer.allocateData(modelDataSize * DataType.FLOAT.getBytes());
        vao.loadDataBuffer(dataBuffer, Attribute.ofPositions(),
                Attribute.ofTexCoords(), Attribute.ofNormals());

        //new Thread(() -> {
        final List<Float> modelData = new ArrayList<>(modelDataSize);
        for (int i = 0; i < vertexCount; i++) {
            for (int j = 0; j < vertexCount; j++) {
                addPositions(modelData, position, size, i, j);
                addTexCoords(modelData, i, j);
                addNormals(modelData, position, size, i, j);
            }
        }
        dataBuffer.storeData(0, MemoryUtils.loadToFloatBuffer(modelData));
        //GlCurrent.addAction(() -> dataVbo.storeData(0, MemoryUtils.loadToFloatBuffer(modelData)));
        //}).start();

        return new SimpleModel(vao, RenderMode.TRIANGLES,
                getIndexBuffer(0),
                getIndexBuffer(1),
                getIndexBuffer(2),
                getIndexBuffer(3),
                getIndexBuffer(5),
                getIndexBuffer(7),
                getIndexBuffer(10),
                getIndexBuffer(15),
                getIndexBuffer(20),
                getIndexBuffer(27),
                getIndexBuffer(35),
                getIndexBuffer(50),
                getIndexBuffer(80),
                getIndexBuffer(120)
        );
    }

    private void addPositions(List<Float> modelData, Vector3fc position, float size, int i, int j) {
        float height = generator.getHeight(
                i / (vertexCount - 1f) * size + position.x(),
                j / (vertexCount - 1f) * size + position.z());
        heights[i][j] = height;
        modelData.add(i / (vertexCount - 1f) * size);
        modelData.add(height);
        modelData.add(j / (vertexCount - 1f) * size);
    }

    private void addTexCoords(List<Float> modelData, int i, int j) {
        modelData.add(j / (vertexCount - 1f));
        modelData.add(i / (vertexCount - 1f));
    }

    private void addNormals(List<Float> modelData, Vector3fc position, float size, int i, int j) {
        float x = i / (vertexCount - 1f) * size + position.x();
        float z = j / (vertexCount - 1f) * size + position.z();
        Vector3f normal = Maths.calculateNormal(
                v1.set(x, generator.getHeight(x, z), z),
                v2.set(x, generator.getHeight(x, z - 1), z - 1),
                v3.set(x - 1, generator.getHeight(x - 1, z), z));
        modelData.add(normal.x);
        modelData.add(normal.y);
        modelData.add(normal.z);
    }

}

package engine.water.generation;

import engine.models.Model;
import engine.models.SimpleModel;
import engine.terrain.Terrain;
import maths.joml.Vector3f;
import maths.objects.Triangle;
import maths.utils.Vector3;
import opengl.constants.DataType;
import opengl.constants.RenderMode;
import opengl.constants.VboUsage;
import opengl.objects.*;
import opengl.utils.MemoryUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class WaveWaterGenerator implements WaterModelGenerator {

    private static final float OFFSET = 0.5f;

    private final Terrain terrain;
    private final int vertexCount;
    private final float terrainSize;

    public WaveWaterGenerator(Terrain terrain, int vertexCount) {
        this.terrain = terrain;
        this.vertexCount = Math.max(vertexCount, 2);
        this.terrainSize = terrain == null ? 1 : terrain.getSize();
    }

    public WaveWaterGenerator(int vertexCount) {
        this(null, vertexCount);
    }

    @Override
    public Model generateModel() {
        final FloatBuffer data = MemoryUtils.allocFloat(vertexCount * vertexCount * 2 * 3);
        final IntBuffer indices = MemoryUtils.allocInt(6 * (vertexCount - 1) * (vertexCount - 1));

        // Determine the positions and the texCoords
        for (int i = 0; i < vertexCount; i++) {
            for (int j = 0; j < vertexCount; j++) {
                data.put(j / (vertexCount - 1f) * terrainSize);
                data.put(i / (vertexCount - 1f) * terrainSize);
            }
        }

        // Determine the Indices
        //final List<Triangle> triangles = new ArrayList<>(indexBuffer.capacity());
        for (int z = 0; z < vertexCount - 1; z++) {
            for (int x = 0; x < vertexCount - 1; x++) {
                int[] corners = {
                        (z * vertexCount) + x, ((z + 1) * vertexCount) + x + 1,
                        (z * vertexCount) + x + 1, ((z + 1) * vertexCount) + x};

                Triangle triangle = readFlatTriangle(data, corners[0], corners[3], corners[2]);
                //triangles.add(triangle);
                if (isAboveTerrain(terrain, triangle)) {
                    indices.put(corners[0]); // Top Left
                    indices.put(corners[3]); // Bottom Left
                    indices.put(corners[2]); // Top Right

                    int i1 = corners[0] * 6;
                    int i2 = corners[3] * 6;
                    int i3 = corners[2] * 6;
                    int i4 = corners[1] * 6; // Im doing it only for 1 triangle

                    data.put(i1 + 2, i3);
                    data.put(i1 + 2 + 1, i3 + 1);
                    data.put(i1 + 2 + 2, i2);
                    data.put(i1 + 2 + 3, i2 + 1);

                    data.put(i2 + 2, i1);
                    data.put(i2 + 2 + 1, i1 + 1);
                    data.put(i2 + 2 + 2, i3);
                    data.put(i2 + 2 + 3, i3 + 1);

                    data.put(i3 + 2, i2);
                    data.put(i3 + 2 + 1, i2 + 1);
                    data.put(i3 + 2 + 2, i1);
                    data.put(i3 + 2 + 3, i1 + 1);
                }

                triangle = readFlatTriangle(data, corners[2], corners[3], corners[1]);
                //triangles.add(triangle);
                if (isAboveTerrain(terrain, triangle)) {
                    indices.put(corners[2]); // Top Right
                    indices.put(corners[3]); // Bottom Left
                    indices.put(corners[1]); // Bottom Right
                }
            }
        }

        indices.flip();
        data.flip();

        final IndexBuffer indexBuffer = BufferUtils.loadToIndexBuffer(
                VboUsage.STATIC_DRAW, indices);

        final DataBuffer dataBuffer = BufferUtils.loadToDataBuffer(
                VboUsage.STATIC_DRAW, data);

        final Vao vao = Vao.create();
        vao.loadDataBuffer(dataBuffer,
                Attribute.of(0, 2, DataType.FLOAT, false),
                Attribute.of(1, 2, DataType.FLOAT, false),
                Attribute.of(2, 2, DataType.FLOAT, false));

        return new SimpleModel(vao, RenderMode.TRIANGLES, indexBuffer);
    }

    private static Triangle readFlatTriangle(FloatBuffer data, int p1, int p2, int p3) {
        Vector3f v1 = Vector3.of(data.get(p1 * 6), 0, data.get(p1 * 6 + 1));
        Vector3f v2 = Vector3.of(data.get(p2 * 6), 0, data.get(p2 * 6 + 1));
        Vector3f v3 = Vector3.of(data.get(p3 * 6), 0, data.get(p3 * 6 + 1));
        return new Triangle(v1, v2, v3);
    }

    private static boolean isAboveTerrain(Terrain terrain, Triangle triangle) {
        return terrain == null
                || isAboveTerrain(terrain, triangle.getP1().x(), triangle.getP1().z())
                || isAboveTerrain(terrain, triangle.getP2().x(), triangle.getP2().z())
                || isAboveTerrain(terrain, triangle.getP3().x(), triangle.getP3().z());
    }

    private static boolean isAboveTerrain(Terrain terrain, float x, float z) {
        x = terrain.getTransform().getPosition().x + x;
        z = terrain.getTransform().getPosition().z + z;
        return terrain.getHeight(x, z) < 0 + OFFSET;
    }

}

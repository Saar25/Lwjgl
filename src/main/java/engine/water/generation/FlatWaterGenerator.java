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

import java.util.Arrays;

public class FlatWaterGenerator implements WaterModelGenerator {

    private static final float OFFSET = 0.5f;

    private final Terrain terrain;
    private final int vertexCount;

    public FlatWaterGenerator(Terrain terrain, int vertexCount) {
        this.terrain = terrain;
        this.vertexCount = vertexCount;
    }

    public FlatWaterGenerator(int vertexCount) {
        this.terrain = null;
        this.vertexCount = vertexCount;
    }

    @Override
    public Model generateModel() {
        float[] positions = new float[vertexCount * vertexCount * 2];
        int[] indices = new int[6 * (vertexCount - 1) * (vertexCount - 1)];
        int pointer;

        // Determine the positions and the texCoords
        pointer = 0;
        for (int i = 0; i < vertexCount; i++) {
            for (int j = 0; j < vertexCount; j++) {
                positions[pointer++] = j / (vertexCount - 1f);
                positions[pointer++] = i / (vertexCount - 1f);
            }
        }

        // Determine the Indices
        pointer = 0;

        //final List<Triangle> triangles = new ArrayList<>(indices.length);
        for (int z = 0; z < vertexCount - 1; z++) {
            for (int x = 0; x < vertexCount - 1; x++) {
                int[] corners = {
                        (z * vertexCount) + x, ((z + 1) * vertexCount) + x + 1,
                        (z * vertexCount) + x + 1, ((z + 1) * vertexCount) + x};

                Triangle triangle = readFlatTriangle(positions, corners[0], corners[3], corners[2]);
                //triangles.add(triangle);
                if (isAboveTerrain(terrain, triangle)) {
                    indices[pointer++] = corners[0]; // Top Left
                    indices[pointer++] = corners[3]; // Bottom Left
                    indices[pointer++] = corners[2]; // Top Right
                }

                triangle = readFlatTriangle(positions, corners[2], corners[3], corners[1]);
                //triangles.add(triangle);
                if (isAboveTerrain(terrain, triangle)) {
                    indices[pointer++] = corners[2]; // Top Right
                    indices[pointer++] = corners[3]; // Bottom Left
                    indices[pointer++] = corners[1]; // Bottom Right
                }
            }
        }

        indices = Arrays.copyOf(indices, pointer);

        final IndexBuffer indexBuffer = BufferUtils.loadToIndexBuffer(
                VboUsage.STATIC_DRAW, indices);

        final DataBuffer dataBuffer = BufferUtils.loadToDataBuffer(
                VboUsage.STATIC_DRAW, positions);

        final Vao vao = Vao.create();
        vao.loadDataBuffer(dataBuffer, Attribute.of(0, 2, DataType.FLOAT, false));

        return new SimpleModel(vao, RenderMode.TRIANGLES, indexBuffer);
    }

    private static Triangle readFlatTriangle(float[] positions, int p1, int p2, int p3) {
        Vector3f v1 = Vector3.of(positions[p1 * 2], 0, positions[p1 * 2 + 1]);
        Vector3f v2 = Vector3.of(positions[p2 * 2], 0, positions[p2 * 2 + 1]);
        Vector3f v3 = Vector3.of(positions[p3 * 2], 0, positions[p3 * 2 + 1]);
        return new Triangle(v1, v2, v3);
    }

    private static Triangle readTriangle(float[] positions, int p1, int p2, int p3) {
        Vector3f v1 = Vector3.of(positions[p1 * 2], positions[p1 * 2 + 1], positions[p1 * 2 + 2]);
        Vector3f v2 = Vector3.of(positions[p2 * 2], positions[p2 * 2 + 1], positions[p2 * 2 + 2]);
        Vector3f v3 = Vector3.of(positions[p3 * 2], positions[p3 * 2 + 1], positions[p3 * 2 + 2]);
        return new Triangle(v1, v2, v3);
    }

    private static boolean isAboveTerrain(Terrain terrain, Triangle triangle) {
        return terrain == null
                || isAboveTerrain(terrain, triangle.getP1().x(), triangle.getP1().z())
                || isAboveTerrain(terrain, triangle.getP2().x(), triangle.getP2().z())
                || isAboveTerrain(terrain, triangle.getP3().x(), triangle.getP3().z());
    }

    private static boolean isAboveTerrain(Terrain terrain, float x, float z) {
        x = terrain.getTransform().getPosition().x + terrain.getSize() * x;
        z = terrain.getTransform().getPosition().z + terrain.getSize() * z;
        return terrain.getHeight(x, z) < 0 + OFFSET;
    }
}

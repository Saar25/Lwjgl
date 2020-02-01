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

public class WaveWaterGeneratorOld implements WaterModelGenerator {

    private static final float OFFSET = 5f;

    private final Terrain terrain;
    private final int vertexCount;
    private final float space;
    private final float size;

    public WaveWaterGeneratorOld(Terrain terrain, int vertexCount) {
        this.terrain = terrain;
        this.vertexCount = vertexCount;
        this.space = 1f / (vertexCount - 1f);
        this.size = terrain == null ? 1 : terrain.getSize();
    }

    public WaveWaterGeneratorOld(int vertexCount) {
        this(null, vertexCount);
    }

    @Override
    public Model generateModel() {
        float[] positions = new float[vertexCount * vertexCount * 2 * 3];
        int[] indices = new int[6 * (vertexCount - 1) * (vertexCount - 1)];
        int pointer;

        // Determine the positions and the texCoords
        pointer = 0;
        for (int i = 0; i < vertexCount; i++) {
            for (int j = 0; j < vertexCount; j++) {
                final float x = j - (vertexCount - 1f) * .5f;
                final float z = i - (vertexCount - 1f) * .5f;
                positions[pointer++] = x * space * size;
                positions[pointer++] = z * space * size;
                pointer += 4;
            }
        }

        // Determine the Indices
        pointer = 0;

        //final List<Triangle> triangles = new ArrayList<>(indices.length);
        for (int x = 0; x < vertexCount - 1; x++) {
            for (int z = 0; z < vertexCount - 1; z++) {
                final int[] corners = {
                        (x * vertexCount) + z, ((x + 1) * vertexCount) + z + 1,
                        (x * vertexCount) + z + 1, ((x + 1) * vertexCount) + z};

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
                    indices[pointer++] = corners[3]; // Bottom Left
                    indices[pointer++] = corners[1]; // Bottom Right
                    indices[pointer++] = corners[2]; // Top Right
                }

                final int i0 = corners[0] * 6;
                final int i1 = corners[1] * 6;
                final int i2 = corners[2] * 6;
                final int i3 = corners[3] * 6;

                positions[i0 + 2] = positions[i2];
                positions[i0 + 3] = positions[i2 + 1];
                positions[i0 + 4] = positions[i3];
                positions[i0 + 5] = positions[i3 + 1];

                positions[i1 + 2] = positions[i3];
                positions[i1 + 3] = positions[i3 + 1];
                positions[i1 + 4] = positions[i2];
                positions[i1 + 5] = positions[i2 + 1];

                positions[i2 + 2] = positions[i1];
                positions[i2 + 3] = positions[i1 + 1];
                positions[i2 + 4] = positions[i0];
                positions[i2 + 5] = positions[i0 + 1];

                positions[i3 + 2] = positions[i0];
                positions[i3 + 3] = positions[i0 + 1];
                positions[i3 + 4] = positions[i1];
                positions[i3 + 5] = positions[i1 + 1];
            }
        }

        indices = Arrays.copyOf(indices, pointer);

        final IndexBuffer indexBuffer = BufferUtils.loadToIndexBuffer(
                VboUsage.STATIC_DRAW, indices);

        final DataBuffer dataBuffer = BufferUtils.loadToDataBuffer(
                VboUsage.STATIC_DRAW, positions);

        final Vao vao = Vao.create();
        vao.loadDataBuffer(dataBuffer,
                Attribute.of(0, 2, DataType.FLOAT, false),
                Attribute.of(1, 2, DataType.FLOAT, false),
                Attribute.of(2, 2, DataType.FLOAT, false));

        return new SimpleModel(vao, RenderMode.TRIANGLES, indexBuffer);
    }

    private static Triangle readFlatTriangle(float[] positions, int p1, int p2, int p3) {
        Vector3f v1 = Vector3.of(positions[p1 * 6], 0, positions[p1 * 6 + 1]);
        Vector3f v2 = Vector3.of(positions[p2 * 6], 0, positions[p2 * 6 + 1]);
        Vector3f v3 = Vector3.of(positions[p3 * 6], 0, positions[p3 * 6 + 1]);
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
        return Float.compare(terrain.getHeight(x, z), OFFSET) <= 0;
    }

}

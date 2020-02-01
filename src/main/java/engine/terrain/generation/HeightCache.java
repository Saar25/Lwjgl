package engine.terrain.generation;

import maths.joml.Vector2f;
import maths.joml.Vector3f;
import maths.utils.Maths;
import maths.utils.Vector2;
import maths.utils.Vector3;

public class HeightCache implements HeightGenerator {

    private static final Vector3f v1 = Vector3.create();
    private static final Vector3f v2 = Vector3.create();
    private static final Vector3f v3 = Vector3.create();

    private final float[][] heights;
    private final HeightGenerator generator;

    private final Vector3f position = Vector3.create();
    private final float size;

    public HeightCache(int vertices, HeightGenerator generator, Vector3f position, float size) {
        this(vertices, vertices, generator, position, size);
    }

    public HeightCache(int cols, int rows, HeightGenerator generator, Vector3f position, float size) {
        this.heights = new float[cols - 1][rows - 1];
        this.generator = generator;
        this.position.set(position);
        this.size = size;

        initialize();
    }

    private void initialize() {
        final int rows = heights.length;
        final int cols = heights[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                final float x = position.x + size * (i / (rows - 1f) - .5f);
                final float z = position.z + size * (j / (cols - 1f) - .5f);
                heights[i][j] = generator.getHeight(x, z);
            }
        }
    }

    private boolean isInside(float x, float z) {
        return Maths.isInside(x, position.x - size / 2, position.x + size / 2)
                && Maths.isInside(z, position.z - size / 2, position.z + size / 2);
    }

    @Override
    public float getHeight(float x, float z) {
        if (!isInside(x, z)) {
            return 0;
        }
        final float terrainX = x - position.x + size / 2;
        final float terrainZ = z - position.z + size / 2;
        final float gridSize = size / (heights.length - 1f);
        final int gridX = (int) (terrainX / gridSize);
        final int gridZ = (int) (terrainZ / gridSize);

        final float xCoord = (terrainX % gridSize) / gridSize;
        final float zCoord = (terrainZ % gridSize) / gridSize;
        final int gridX1 = Math.min(gridX + 1, heights.length - 1);
        final int gridZ1 = Math.min(gridZ + 1, heights.length - 1);
        final Vector2f coord = Vector2.of(xCoord, zCoord);
        if (xCoord <= (1 - zCoord)) {
            v1.set(0, heights[gridX][gridZ], 0);
            v2.set(1, heights[gridX1][gridZ], 0);
            v3.set(0, heights[gridX][gridZ1], 1);
            return Maths.barryCentric(v1, v2, v3, coord);
        } else {
            v1.set(1, heights[gridX1][gridZ], 0);
            v2.set(1, heights[gridX1][gridZ1], 1);
            v3.set(0, heights[gridX][gridZ1], 1);
            return Maths.barryCentric(v1, v2, v3, coord);
        }
    }
}

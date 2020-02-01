package engine.models;

import engine.shape.generators.*;
import maths.joml.Vector3f;
import maths.noise.Noise3f;
import maths.objects.Triangle;
import maths.utils.Vector3;
import opengl.constants.DataType;
import opengl.constants.RenderMode;
import opengl.constants.VboUsage;
import opengl.objects.*;

import java.util.Arrays;
import java.util.function.Predicate;

public final class ModelGenerator {

    private ModelGenerator() {

    }

    /**
     * Generates a square model
     *
     * @return a square model
     */
    public static Model generateSquare() {
        return squareGenerator().generateModel();
    }

    public static SquareGenerator squareGenerator() {
        return SquareGenerator.getInstance();
    }

    /**
     * Generates a cube model
     *
     * @return a cube model
     */
    public static Model generateCube() {
        return cubeGenerator().generateModel();
    }

    public static CubeGenerator cubeGenerator() {
        return CubeGenerator.getInstance();
    }

    /**
     * Generate a triangle model
     *
     * @return a triangle model
     */
    public static Model generateTriangle() {
        return triangleGenerator().generateModel();
    }

    public static TriangleGenerator triangleGenerator() {
        return TriangleGenerator.getInstance();
    }

    public static TriangleGenerator triangleGenerator(Triangle triangle) {
        return new TriangleGenerator(triangle);
    }

    /**
     * Generate a triangle model
     *
     * @return a triangle model
     */
    public static Model generatePlane() {
        return planeGenerator().generateModel();
    }

    public static PlaneGenerator planeGenerator() {
        return PlaneGenerator.getInstance();
    }

    public static PlaneGenerator planeGenerator(int vertices) {
        return new PlaneGenerator(vertices);
    }

    /**
     * Generate a line model
     *
     * @return a line model
     */
    public static Model generateLine() {
        return lineGenerator().generateModel();
    }

    public static LineGenerator lineGenerator() {
        return LineGenerator.getInstance();
    }

    public static LineGenerator lineGenerator(float x1, float y1, float x2, float y2) {
        return new LineGenerator(x1, y1, x2, y2);
    }

    /**
     * Generate a sphere model
     *
     * @return a sphere model
     */
    public static Model generateSphere() {
        return sphereGenerator().generateModel();
    }

    public static SphereGenerator sphereGenerator() {
        return SphereGenerator.getInstance();
    }

    public static SphereGenerator sphereGenerator(int stacks, int sectors) {
        return new SphereGenerator(stacks, sectors);
    }

    public static SphereGenerator sphereGenerator(int stacks, int sectors, Noise3f noise) {
        return new SphereGenerator(stacks, sectors, noise);
    }

    /**
     * Generate a sphere model
     *
     * @return a sphere model
     */
    public static Model generateSpherifiedCube() {
        return spherifiedCubeGenerator().generateModel();
    }

    public static SpherifiedCubeGenerator spherifiedCubeGenerator() {
        return SpherifiedCubeGenerator.getInstance();
    }

    public static SpherifiedCubeGenerator spherifiedCubeGenerator(int vertices) {
        return new SpherifiedCubeGenerator(vertices);
    }

    /**
     * Generate flat plane mesh with the given amount of vertices on each side of the quad
     *
     * @param vertices the amount of vertices on each side of the quad
     * @param test     test if the triangle should be added to the mesh
     * @return the generated model
     */
    public static Model generatePlane(int vertices, Predicate<Triangle> test) {
        vertices = Math.max(vertices, 2);
        float[] positions = new float[vertices * vertices * 2];
        int[] indices = new int[6 * (vertices - 1) * (vertices - 1)];
        int pointer;

        // Determine the positions and the texCoords
        pointer = 0;
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                positions[pointer++] = j / (vertices - 1f);
                positions[pointer++] = i / (vertices - 1f);
            }
        }

        // Determine the Indices
        pointer = 0;

        //final List<Triangle> triangles = new ArrayList<>(indices.length);
        for (int z = 0; z < vertices - 1; z++) {
            for (int x = 0; x < vertices - 1; x++) {
                int[] corners = {
                        (z * vertices) + x, ((z + 1) * vertices) + x + 1,
                        (z * vertices) + x + 1, ((z + 1) * vertices) + x};

                Triangle triangle = readFlatTriangle(positions, corners[0], corners[3], corners[2]);
                //triangles.add(triangle);
                if (test.test(triangle)) {
                    indices[pointer++] = corners[0]; // Top Left
                    indices[pointer++] = corners[3]; // Bottom Left
                    indices[pointer++] = corners[2]; // Top Right
                }

                triangle = readFlatTriangle(positions, corners[2], corners[3], corners[1]);
                //triangles.add(triangle);
                if (test.test(triangle)) {
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

}

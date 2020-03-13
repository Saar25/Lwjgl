package engine.terrain.lowpoly;

import engine.models.Model;
import engine.models.SimpleModel;
import engine.shape.generators.IModelGenerator;
import engine.terrain.generation.SimplexNoise;
import maths.joml.Vector3f;
import maths.noise.Noise3f;
import maths.utils.Vector3;
import opengl.constants.DataType;
import opengl.constants.RenderMode;
import opengl.constants.VboUsage;
import opengl.objects.*;
import opengl.utils.MemoryUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class LowPolyPlanetModelGenerator implements IModelGenerator {

    private final int vertexCount;
    private final float space;
    private final float size;
    private float roughness = 10f;

    private final Noise3f simplexNoise = new SimplexNoise(10, 1, 5);
    private final Noise3f heightsGenerator = (x, y, z) -> {
        float noise = 0;
        for (int i = 1; i < 5; i++) {
            noise += simplexNoise.noise(Vector3.of(x, y, z).mul(i * i)) / (i * i);
        }
        return noise;
    };

    public LowPolyPlanetModelGenerator(int vertexCount, float size) {
        this.vertexCount = vertexCount;
        this.space = 1f / (vertexCount - 1);
        this.size = size;
    }

    public static Model generateModel(int vertexCount, float size) {
        return new LowPolyPlanetModelGenerator(vertexCount, size).generateModel();
    }

    public static Model generateModel(int vertexCount, float size, float roughness) {
        LowPolyPlanetModelGenerator generator = new LowPolyPlanetModelGenerator(vertexCount, size);
        generator.setRoughness(roughness);
        return generator.generateModel();
    }

    public void setRoughness(float roughness) {
        this.roughness = roughness;
    }

    @Override
    public Vao createVao() {
        final Vao vao = Vao.create();
        vao.loadIndexBuffer(createIndexVbo());
        vao.loadDataBuffer(createDataVbo(),
                Attribute.of(0, 3, DataType.FLOAT, false),  // position
                Attribute.of(1, 3, DataType.FLOAT, false),  // colour
                Attribute.of(2, 3, DataType.FLOAT, false)); // normal
        return vao;
    }

    public FloatBuffer createDataBuffer() {
        final FloatBuffer dataBuffer = MemoryUtils.allocFloat(
                ((vertexCount - 1) * (vertexCount - 1) + vertexCount * vertexCount) * 9 * 6);
        putFace(dataBuffer, Vector3.of(1, 0, 0), Vector3.of(0, 0, 1));
        putFace(dataBuffer, Vector3.of(1, 0, 0), Vector3.of(0, 1, 0));
        putFace(dataBuffer, Vector3.of(0, 1, 0), Vector3.of(0, 0, 1));
        putFace(dataBuffer, Vector3.of(0, 1, 0), Vector3.of(1, 0, 0));
        putFace(dataBuffer, Vector3.of(0, 0, 1), Vector3.of(1, 0, 0));
        putFace(dataBuffer, Vector3.of(0, 0, 1), Vector3.of(0, 1, 0));
        dataBuffer.flip();
        return dataBuffer;
    }

    @Override
    public DataBuffer createDataVbo() {
        return BufferUtils.loadToDataBuffer(VboUsage.STATIC_DRAW, createDataBuffer());
    }

    private void putFace(FloatBuffer dataBuffer, Vector3f v1, Vector3f v2) {
        final Vector3f v3 = Vector3.cross(v2, v1);
        for (int i = 0; i < vertexCount; i++) {
            final float vi = i * space - .5f;
            for (int j = 0; j < vertexCount; j++) {
                final float vj = j * space - .5f;
                float x = v1.x * vi + v2.x * vj + v3.x * .5f;
                float y = v1.y * vi + v2.y * vj + v3.y * .5f;
                float z = v1.z * vi + v2.z * vj + v3.z * .5f;
                putVertex(dataBuffer, x, y, z, v1, v2);
            }
        }
        for (int i = 0; i < vertexCount - 1; i++) {
            final float vi = (i + .5f) * space - .5f;
            for (int j = 0; j < vertexCount - 1; j++) {
                final float vj = (j + .5f) * space - .5f;
                float x = v1.x * vi + v2.x * vj + v3.x * .5f;
                float y = v1.y * vi + v2.y * vj + v3.y * .5f;
                float z = v1.z * vi + v2.z * vj + v3.z * .5f;
                putVertex(dataBuffer, x, y, z, v1, v2);
            }
        }
    }

    private float height(Vector3f vertex) {
        final float min = 5f;
        final float amplitude = 20f;

        final Vector3f normalized = Vector3.normalize(vertex);
        float height = heightsGenerator.noise(normalized.mul(roughness));
        //height = 1 - Math.abs(height);
        height = height * amplitude;
        height = Math.max(height, min);
        return size + height;
    }

    private void putVertex(FloatBuffer dataBuffer, float x, float y, float z, Vector3f v1, Vector3f v2) {

        final Vector3f position = Vector3.of(x, y, z);
        position.normalize(height(position));

        final Vector3f position1 = Vector3.add(position, v1);
        position1.normalize(height(position1));

        final Vector3f position2 = Vector3.add(position, v2);
        position2.normalize(height(position2));

        final Vector3f vec1 = position1.sub(position).normalize();
        final Vector3f vec2 = position2.sub(position).normalize();
        final Vector3f normal = vec2.cross(vec1).normalize();

        //final Vector3f normal = Vector3.of(x, y, z);
        final Vector3f colour = Vector3.of(.5f, .6f, 0);

        dataBuffer.put(position.x).put(position.y).put(position.z);
        dataBuffer.put(colour.x).put(colour.y).put(colour.z);
        dataBuffer.put(normal.x).put(normal.y).put(normal.z);
    }

    public IntBuffer createIndexBuffer() {
        final IntBuffer indexBuffer = MemoryUtils.allocInt((vertexCount - 1) * (vertexCount - 1) * 4 * 3 * 6);
        int faceOffset = 0;
        for (int face = 0; face < 6; face++) {
            for (int x = 0; x < vertexCount - 1; x++) {
                for (int z = 0; z < vertexCount - 1; z++) {
                    final int center = vertexCount * vertexCount + x + z * (vertexCount - 1);
                    final int[] indices = {x + z * vertexCount, x + 1 + z * vertexCount,
                            x + 1 + (z + 1) * vertexCount, x + (z + 1) * vertexCount};

                    for (int i = 0; i < 4; i++) {
                        indexBuffer.put(faceOffset + indices[(i + 1) % 4]);
                        indexBuffer.put(faceOffset + center);
                        indexBuffer.put(faceOffset + indices[i]);
                    }
                }
            }
            faceOffset += ((vertexCount - 1) * (vertexCount - 1) + vertexCount * vertexCount);
        }
        indexBuffer.flip();
        return indexBuffer;
    }

    @Override
    public IndexBuffer createIndexVbo() {
        return createIndexVbo(0);
    }

    @Override
    public IndexBuffer createIndexVbo(int lod) {
        return BufferUtils.loadToIndexBuffer(VboUsage.STATIC_DRAW, createIndexBuffer());
    }

    @Override
    public Model generateModel() {
        return new SimpleModel(createVao(), RenderMode.TRIANGLES);
    }
}

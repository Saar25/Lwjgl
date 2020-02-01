package engine.terrain.lowpoly;

import engine.models.Model;
import engine.models.SimpleModel;
import engine.shape.generators.IModelGenerator;
import engine.terrain.TerrainVertex;
import engine.terrain.generation.ColourGenerator;
import engine.terrain.generation.HeightGenerator;
import engine.terrain.generation.TerrainModelGenerator;
import maths.joml.Vector3f;
import maths.joml.Vector3fc;
import maths.objects.Box;
import maths.utils.Maths;
import maths.utils.Vector3;
import opengl.constants.DataType;
import opengl.constants.RenderMode;
import opengl.constants.VboUsage;
import opengl.objects.*;
import opengl.utils.MemoryUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class LPTerrainModelGenerator implements TerrainModelGenerator, IModelGenerator {

    private static final boolean randomize = true;

    private static final Vector3f v1 = Vector3.create();
    private static final Vector3f v2 = Vector3.create();
    private static final Vector3f v3 = Vector3.create();

    private final int vertexCount;
    private final IndexBuffer[] levels;
    private final float space;

    private final HeightGenerator heightGenerator;
    private final ColourGenerator colourGenerator;

    private final Vector3f position = Vector3.create();
    private float terrainSize;

    public LPTerrainModelGenerator(int vertexCount, HeightGenerator heightGenerator, ColourGenerator colourGenerator, int... levels) {
        this.vertexCount = vertexCount;
        this.space = 1f / (vertexCount - 1);
        this.heightGenerator = heightGenerator;
        this.colourGenerator = colourGenerator;

        this.levels = levels.length > 0 ? new IndexBuffer[levels.length]
                : new IndexBuffer[]{createIndexVbo()};
        for (int i = 0; i < levels.length; i++) {
            this.levels[i] = createIndexVbo(levels[i]);
        }
    }

    public static Model generateModel(LPTerrainConfigs configs) {
        final LPTerrainModelGenerator generator = new LPTerrainModelGenerator(configs.vertices,
                configs.heightGenerator, configs.colourGenerator, configs.levels);
        return generator.generateModel(configs.position, configs.size);
    }

    @Override
    public Vao createVao() {
        final Vao vao = Vao.create();
        vao.loadIndexBuffer(levels[0]);
        vao.loadDataBuffer(createDataVbo(),
                Attribute.of(0, 3, DataType.FLOAT, false),  // position
                Attribute.of(1, 3, DataType.FLOAT, false),  // colour
                Attribute.of(2, 3, DataType.FLOAT, false)); // normal
        return vao;
    }

    @Override
    public IVbo createDataVbo() {
        final FloatBuffer buffer = MemoryUtils.allocFloat(
                ((vertexCount - 1) * (vertexCount - 1) + vertexCount * vertexCount) * 9);

        for (int x = 0; x < vertexCount; x++) {
            final float vx = x * space - .5f;
            for (int z = 0; z < vertexCount; z++) {
                final float vz = z * space - .5f;
                putVertex(buffer, vx, vz);
            }
        }
        for (int x = 0; x < vertexCount - 1; x++) {
            final float vx = (x + .5f) * space - .5f;
            for (int z = 0; z < vertexCount - 1; z++) {
                final float vz = (z + .5f) * space - .5f;
                putVertex(buffer, vx, vz);
            }
        }
        buffer.flip();

        return BufferUtils.loadToDataBuffer(VboUsage.STATIC_DRAW, buffer);
    }

    private void putVertex(FloatBuffer dataBuffer, float x, float z) {
        final float tSpace = space * terrainSize;
        final float tx = position.x + x * terrainSize;
        final float tz = position.z + z * terrainSize;
        final float height = heightGenerator.getHeight(tx, tz);

        final Vector3f normal = Maths.calculateNormal(v1.set(tx, height, tz),
                v2.set(tx, heightGenerator.getHeight(tx, tz - tSpace), tz - tSpace),
                v3.set(tx - tSpace, heightGenerator.getHeight(tx - tSpace, tz), tz));
        final TerrainVertex vertex = new LPTerrainVertex(Vector3.of(tx, height, tz), normal);
        final Vector3f colour = colourGenerator.generateColour(vertex);

        dataBuffer.put(x * terrainSize).put(height).put(z * terrainSize);
        dataBuffer.put(colour.x).put(colour.y).put(colour.z);
        dataBuffer.put(normal.x).put(normal.y).put(normal.z);
    }

    @Override
    public IndexBuffer createIndexVbo() {
        return createIndexVbo(0);
    }

    @Override
    public LPTerrainModel generateModel() {
        final Vao vao = Vao.create();
        vao.loadIndexBuffer(levels[0]);

        final IVbo dataVbo = createDataVbo();
        vao.loadDataBuffer(dataVbo,
                Attribute.of(0, 3, DataType.FLOAT, false),  // position
                Attribute.of(1, 3, DataType.FLOAT, false),  // colour
                Attribute.of(2, 3, DataType.FLOAT, false)); // normal

        final SimpleModel model = new SimpleModel(createVao(), RenderMode.TRIANGLES, levels);
        return new LPTerrainModel(model, vao, dataVbo);
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

                final int[] indices = {x + z * vertexCount, x + incX + z * vertexCount,
                        x + incX + (z + incZ) * vertexCount, x + (z + incZ) * vertexCount};

                final int center = inc % 2 == 0 ? x + incX / 2 + (z + incZ / 2) * vertexCount
                        : vertexCount * vertexCount + x + incX / 2 + (z + incZ / 2) * (vertexCount - 1);

                for (int i = 0; i < 4; i++) {
                    final int order = randomize ? (int) (Math.random() * 3) : 0;
                    if (order == 0) {
                        indexBuffer.put(indices[(i + 1) % 4]);
                        indexBuffer.put(center);
                        indexBuffer.put(indices[i]);
                    } else if (order == 1) {
                        indexBuffer.put(center);
                        indexBuffer.put(indices[i]);
                        indexBuffer.put(indices[(i + 1) % 4]);
                    } else {
                        indexBuffer.put(indices[i]);
                        indexBuffer.put(indices[(i + 1) % 4]);
                        indexBuffer.put(center);
                    }
                }
            }
        }
        indexBuffer.flip();

        return BufferUtils.loadToIndexBuffer(VboUsage.STATIC_DRAW, indexBuffer);
    }

    @Override
    public Model generateModel(Vector3fc position, float size) {
        this.terrainSize = size;
        this.position.set(position);

        final Vao vao = Vao.create();
        vao.loadIndexBuffer(levels[0]);
        final IVbo dataVbo = createDataVbo();
        vao.loadDataBuffer(dataVbo,
                Attribute.of(0, 3, DataType.FLOAT, false),  // position
                Attribute.of(1, 3, DataType.FLOAT, false),  // colour
                Attribute.of(2, 3, DataType.FLOAT, false)); // normal

        final Vector3f min = Vector3.of(-size / 2f);
        final Vector3f max = Vector3.of(+size / 2f);
        final SimpleModel model = new SimpleModel(createVao(),
                RenderMode.TRIANGLES, Box.of(min, max), levels);
        return new LPTerrainModel(model, vao, dataVbo);
    }
}

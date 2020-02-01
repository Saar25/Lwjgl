package engine.terrain;

import engine.models.Model;
import engine.models.ModelGenerator;
import engine.models.SimpleModel;
import engine.models.Skin;
import engine.shape.generators.PlaneGenerator;
import maths.joml.Vector3f;
import opengl.constants.RenderMode;
import opengl.objects.IndexBuffer;

public class FlatTerrain extends Terrain {

    public FlatTerrain(Vector3f position, float size) {
        super(Skin.create(), size);
        getTransform().setPosition(position);
        getTransform().setRotation(90, 0, 0);
        getTransform().setScale(size);
    }

    @Override
    protected Model createModel() {
        final PlaneGenerator planeGenerator = ModelGenerator.planeGenerator(1025);
        return new SimpleModel(planeGenerator.createVao(), RenderMode.TRIANGLES,
                planeGenerator.createIndexVbo(2),
                planeGenerator.createIndexVbo(4),
                planeGenerator.createIndexVbo(8),
                planeGenerator.createIndexVbo(16),
                planeGenerator.createIndexVbo(32),
                planeGenerator.createIndexVbo(64),
                planeGenerator.createIndexVbo(128),
                planeGenerator.createIndexVbo(256),
                planeGenerator.createIndexVbo(512),
                planeGenerator.createIndexVbo(1024),
                IndexBuffer.NULL
        );
    }

    @Override
    public void process() {
        TerrainRenderer.getInstance().process(this);
    }

    @Override
    public float getHeight(float x, float z) {
        return getTransform().getPosition().y;
    }
}

package engine.terrain.triangle;

import engine.models.Model;
import engine.models.ModelGenerator;
import engine.models.SimpleModel;
import engine.rendering.RenderContext;
import engine.terrain.Terrain;
import engine.terrain.lowpoly.LowPolyTerrainRenderer;
import maths.joml.Matrix4f;
import maths.utils.Matrix4;
import opengl.constants.DataType;
import opengl.constants.RenderMode;
import opengl.constants.VboUsage;
import opengl.objects.Attribute;
import opengl.objects.DataBuffer;
import opengl.objects.Vao;
import opengl.shaders.RenderState;
import opengl.utils.DynamicFloatBuffer;
import opengl.utils.GlUtils;

public class TriangularTerrainRenderer extends LowPolyTerrainRenderer {

    private DataBuffer vbo = new DataBuffer(VboUsage.DYNAMIC_DRAW);
    private Vao vao = ModelGenerator.triangleGenerator().createVao();
    private Model model = new SimpleModel(vao, RenderMode.TRIANGLES);

    private DynamicFloatBuffer floatBuffer = new DynamicFloatBuffer(32 * 10000);

    public TriangularTerrainRenderer() throws Exception {
        super();

        vbo.allocateFloat(floatBuffer.get().capacity());
        vao.loadDataBuffer(vbo,
                Attribute.ofInstance(3, 4, DataType.FLOAT, false),
                Attribute.ofInstance(4, 4, DataType.FLOAT, false),
                Attribute.ofInstance(5, 4, DataType.FLOAT, false),
                Attribute.ofInstance(6, 4, DataType.FLOAT, false),

                Attribute.ofInstance(8, 4, DataType.FLOAT, false),
                Attribute.ofInstance(9, 4, DataType.FLOAT, false),
                Attribute.ofInstance(10, 4, DataType.FLOAT, false),
                Attribute.ofInstance(11, 4, DataType.FLOAT, false)
        );
    }

    @Override
    public void render(RenderContext context) {
        getShadersProgram().bind();

        GlUtils.enableCulling();
        GlUtils.enableDepthTest();
        GlUtils.disableBlending();

        RenderState<Terrain> renderState = new RenderState<>(this, context.getCamera());
        getShadersProgram().updatePerRenderUniforms(renderState);

        floatBuffer.get().flip();
        vbo.storeData(0, floatBuffer.get());
        model.render();

        getShadersProgram().unbind();
        floatBuffer.get().clear();
    }

    @Override
    public void process(Terrain terrain) {
        floatBuffer.ensureCapacity(16);
        Matrix4f transformation = terrain.getTransform().getTransformationMatrix();
        Matrix4f shadowSpace = Matrix4.pool.poolAndGive();
        if (getContext().getSun() != null) {
            getContext().getSun().toViewSpace(transformation, shadowSpace);
        }
        transformation.get(floatBuffer.get()).position(floatBuffer.get().position() + 16);
        shadowSpace.get(floatBuffer.get()).position(floatBuffer.get().position() + 16);
        super.process(terrain);
    }
}

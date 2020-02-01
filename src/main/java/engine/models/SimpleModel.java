package engine.models;

import maths.objects.Box;
import opengl.constants.DataType;
import opengl.constants.RenderMode;
import opengl.objects.IVbo;
import opengl.objects.Vao;
import opengl.utils.GlRendering;

public class SimpleModel implements Model {

    private final Vao vao;
    private final RenderMode renderMode;

    private final Lod lod;
    private final Box bounds;

    public SimpleModel(Vao vao, RenderMode renderMode) {
        this(vao, renderMode, Box.of(0, 0, 0));
    }

    public SimpleModel(Vao vao, RenderMode renderMode, IVbo... indexBuffers) {
        this(vao, renderMode, Box.of(0, 0, 0), indexBuffers);
    }

    public SimpleModel(Vao vao, RenderMode renderMode, Box bounds) {
        this.vao = vao;
        this.renderMode = renderMode;
        this.lod = new Lod();
        this.bounds = bounds;
    }

    public SimpleModel(Vao vao, RenderMode renderMode, Box bounds, IVbo... indexBuffers) {
        this.vao = vao;
        this.renderMode = renderMode;
        this.lod = new Lod(indexBuffers);
        this.bounds = bounds;
    }

    @Override
    public void render() {
        if (getLod().available()) {
            IVbo indexBuffer = getLod().current();
            vao.loadIndexBuffer(indexBuffer, false);
        }
        vao.bind();
        vao.enableAttributes();

        if (vao.hasIndices()) {
            GlRendering.drawElements(renderMode,
                    vao.getIndexCount(), DataType.U_INT, 0);
        } else {
            GlRendering.drawArrays(renderMode, 0, vao.getIndexCount());
        }
        vao.unbind();
    }

    @Override
    public Lod getLod() {
        return lod;
    }

    @Override
    public Box getBounds() {
        return bounds;
    }

    @Override
    public void delete() {
        this.vao.delete(true);
    }
}

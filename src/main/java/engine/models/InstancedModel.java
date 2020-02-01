package engine.models;

import maths.objects.Box;
import opengl.constants.DataType;
import opengl.constants.RenderMode;
import opengl.objects.Vao;
import opengl.objects.Vbo;
import opengl.utils.GlRendering;

public class InstancedModel implements Model {

    private final Vao vao;
    private final RenderMode renderMode;
    private final int vertices;

    private final Lod lod;
    private final Box bounds;

    private int instances;

    public InstancedModel(Vao vao, RenderMode renderMode, int vertices) {
        this(vao, renderMode, vertices, Box.of(0, 0, 0));
    }

    public InstancedModel(Vao vao, RenderMode renderMode, int vertices, Box bounds) {
        this.vao = vao;
        this.renderMode = renderMode;
        this.vertices = vertices;
        this.bounds = bounds;
        this.lod = new Lod();
    }

    public InstancedModel(Vao vao, RenderMode renderMode, int vertices, Vbo... indexBuffers) {
        this(vao, renderMode, vertices, Box.of(0, 0, 0), indexBuffers);
    }

    public InstancedModel(Vao vao, RenderMode renderMode, int vertices, Box bounds, Vbo... indexBuffers) {
        this.vao = vao;
        this.renderMode = renderMode;
        this.vertices = vertices;
        this.bounds = bounds;
        this.lod = new Lod(indexBuffers);
    }

    public void setInstances(int instances) {
        this.instances = instances;
    }

    @Override
    public void render() {
        this.vao.bind();
        this.vao.enableAttributes();
        if (this.vao.hasIndices()) {
            GlRendering.drawElementsInstanced(renderMode,
                    vertices, DataType.U_INT, 0, instances);
        } else {
            GlRendering.drawArraysInstanced(renderMode, 0,
                    vertices, instances);
        }
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
        vao.delete(true);
    }
}

package opengl.objects;

import opengl.utils.GlConfigs;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

public class Vao {

    private static final Vao NULL = new Vao(0);
    private static final Vao FIRST = Vao.create();

    private static int boundVao = 0;

    private final int id;

    private IVbo indexBuffer;
    private final List<IVbo> dataBuffers;
    private final List<Attribute> attributes;

    private boolean deleted = false;

    private Vao(int id) {
        this.id = id;
        this.dataBuffers = new ArrayList<>();
        this.attributes = new ArrayList<>();
        this.bind();
    }

    public static Vao create() {
        int id = GL30.glGenVertexArrays();
        return new Vao(id);
    }

    /**
     * Binds a vao if no vao is bound, useful for rendering meshes that do not use vaos
     */
    public static void bindIfNone() {
        if (boundVao == 0) {
            FIRST.bind();
        }
    }

    /**
     * Returns whether the vao is bound
     *
     * @return true if bound false otherwise
     */
    public boolean isBound() {
        return boundVao == id;
    }

    /**
     * Enables the vao attributes
     */
    public void enableAttributes() {
        attributes.forEach(Attribute::enable);
    }

    /**
     * Disables the vao attributes
     */
    public void disableAttributes() {
        attributes.forEach(Attribute::disable);
    }

    /**
     * Returns whether this vao has indices buffer
     *
     * @return true if has indices, else false
     */
    public boolean hasIndices() {
        return indexBuffer != null;
    }

    /**
     * Loads an existed vbo and linking the given attributes to this vbo
     *
     * @param vbo        vbo that holds the data needed
     * @param attributes the attributes of this vbo
     */
    public void loadDataBuffer(IVbo vbo, Attribute... attributes) {
        vbo.bindToVao(this);
        linkAttributes(attributes);
        dataBuffers.add(vbo);
    }

    /**
     * Loads an existing index buffer and link him to this vao
     *
     * @param indexBuffer the index buffer
     */
    public void loadIndexBuffer(IVbo indexBuffer) {
        loadIndexBuffer(indexBuffer, true);
    }

    /**
     * Loads an existing index buffer and link him to this vao
     *
     * @param indexBuffer the index buffer
     * @param deleteOld   true if old index buffer should be deleted
     */
    public void loadIndexBuffer(IVbo indexBuffer, boolean deleteOld) {
        if (this.indexBuffer != null && deleteOld) {
            this.indexBuffer.delete();
        }
        this.indexBuffer = indexBuffer;
        this.indexBuffer.bindToVao(this);
    }

    /**
     * Returns the amount of indices in this vao
     *
     * @return the amount of indices
     */
    public int getIndexCount() {
        return hasIndices() ? (int) indexBuffer.getSize() : 0;
    }

    /**
     * Binds the vao
     */
    public void bind() {
        if (GlConfigs.CACHE_STATE || !isBound()) {
            GL30.glBindVertexArray(id);
            boundVao = id;
        }
    }

    /**
     * Unbind the vao
     */
    public void unbind() {
        if (GlConfigs.CACHE_STATE || isBound()) {
            GL30.glBindVertexArray(0);
            boundVao = 0;
        }
    }

    /**
     * Delete this vao and his related vbos if needed
     *
     * @param deleteVbos true if need to delete the related vbo, false otherwise
     */
    public void delete(boolean deleteVbos) {
        if (deleteVbos) {
            if (indexBuffer != null) {
                indexBuffer.delete();
            }
            dataBuffers.forEach(IVbo::delete);
        }
        if (GlConfigs.CACHE_STATE || !deleted) {
            GL30.glDeleteVertexArrays(id);
            deleted = true;
        }
    }

    /**
     * Link the attributes, assuming that the data in the vbo is stored as [v1data, v2data, v3data...]
     * and not as [[positions], [texture coordinate], [normals], ...]
     *
     * @param attributes the attributes to link
     */
    private void linkAttributes(Attribute... attributes) {
        int offset = 0;
        int stride = getBytesPerVertex(attributes);
        for (Attribute attribute : attributes) {
            attribute.link(stride, offset);
            offset += attribute.getBytesPerVertex();
            this.attributes.add(attribute);
        }
    }

    /**
     * Return the amount of bytes allocated for each vertex
     *
     * @param attributes the attributes related to the vbo
     * @return amount of bytes per vertex
     */
    private int getBytesPerVertex(Attribute... attributes) {
        int sizeInBytes = 0;
        for (Attribute attribute : attributes) {
            sizeInBytes += attribute.getBytesPerVertex();
        }
        return sizeInBytes;
    }
}
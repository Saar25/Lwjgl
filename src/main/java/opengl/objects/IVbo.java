package opengl.objects;

public interface IVbo {

    /**
     * Return the size of the vbo in bytes
     *
     * @return the size of the vbo int bytes
     */
    long getSize();

    /**
     * Bind the vbo
     */
    void bind();

    /**
     * Bind the vbo the the vao
     *
     * @param vao the vao
     */
    void bindToVao(Vao vao);

    /**
     * Unbind the vbo
     */
    void unbind();

    /**
     * Delete the vbo
     */
    void delete();

}

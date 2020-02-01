package opengl.textures;

public interface ITexture {

    /**
     * Binds the texture to a specific texture unit
     *
     * @param unit the texture active to active and bind to
     */
    void bind(int unit);

    /**
     * Binds the texture to the current active texture
     */
    void bind();

    /**
     * Unbinds the texture from the current active texture
     */
    void unbind();

    /**
     * Deletes the texture
     */
    void delete();

}

package opengl.textures;

import opengl.textures.parameters.MagFilterParameter;
import opengl.textures.parameters.MinFilterParameter;
import opengl.textures.parameters.WrapParameter;

public class CubeMapTextureBuilder {

    private TextureInfo positiveX;
    private TextureInfo negativeX;
    private TextureInfo positiveY;
    private TextureInfo negativeY;
    private TextureInfo positiveZ;
    private TextureInfo negativeZ;

    /**
     * Sets the texture on the positive x
     *
     * @param textureFile the texture's file
     * @return this
     */
    public CubeMapTextureBuilder positiveX(String textureFile) throws Exception {
        positiveX = TextureLoader.load(textureFile);
        return this;
    }

    /**
     * Sets the texture on the negative x
     *
     * @param textureFile the texture's file
     * @return this
     */
    public CubeMapTextureBuilder negativeX(String textureFile) throws Exception {
        negativeX = TextureLoader.load(textureFile);
        return this;
    }

    /**
     * Sets the texture on the positive y
     *
     * @param textureFile the texture's file
     * @return this
     */
    public CubeMapTextureBuilder positiveY(String textureFile) throws Exception {
        positiveY = TextureLoader.load(textureFile);
        return this;
    }

    /**
     * Sets the texture on the negative y
     *
     * @param textureFile the texture's file
     * @return this
     */
    public CubeMapTextureBuilder negativeY(String textureFile) throws Exception {
        negativeY = TextureLoader.load(textureFile);
        return this;
    }

    /**
     * Sets the texture on the positive z
     *
     * @param textureFile the texture's file
     * @return this
     */
    public CubeMapTextureBuilder positiveZ(String textureFile) throws Exception {
        positiveZ = TextureLoader.load(textureFile);
        return this;
    }

    /**
     * Sets the texture on the negative z
     *
     * @param textureFile the texture's file
     * @return this
     */
    public CubeMapTextureBuilder negativeZ(String textureFile) throws Exception {
        negativeZ = TextureLoader.load(textureFile);
        return this;
    }

    public CubeMapTexture create() {
        CubeMapTexture texture = CubeMapTexture.of(positiveX, positiveY, positiveZ, negativeX, negativeY, negativeZ);
        TextureFunctions function = new TextureFunctions(texture, TextureTarget.TEXTURE_CUBE_MAP);
        function.minFilter(MinFilterParameter.LINEAR)
                .magFilter(MagFilterParameter.LINEAR)
                .wrapS(WrapParameter.CLAMP_TO_EDGE)
                .wrapT(WrapParameter.CLAMP_TO_EDGE);
        return texture;
    }

}

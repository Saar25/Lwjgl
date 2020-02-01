package opengl.textures;

import maths.joml.Vector4f;
import opengl.constants.DataType;
import opengl.constants.FormatType;
import opengl.textures.parameters.MagFilterParameter;
import opengl.textures.parameters.MinFilterParameter;
import opengl.textures.parameters.WrapParameter;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;

/**
 * The getTexture() functions class holds all the functions that can effect a getTexture()
 *
 * @author Saar ----
 * @version 1.0
 * @since 2018-10-6
 */
public final class TextureFunctions {

    private final ITexture texture;
    private final int target;

    public TextureFunctions(ITexture texture, TextureTarget target) {
        this.texture = texture;
        this.target = target.get();
    }

    public void apply(TextureConfigs configs) {
        if (configs.mipmap) {
            generateMipmap();
        }
        if (configs.minFilter != null) {
            minFilter(configs.minFilter);
        }
        if (configs.magFilter != null) {
            magFilter(configs.magFilter);
        }
        if (configs.wrapS != null) {
            wrapS(configs.wrapS);
        }
        if (configs.wrapT != null) {
            wrapT(configs.wrapT);
        }
        if (configs.levelOfDetailBias != 0) {
            levelOfDetailBias(configs.levelOfDetailBias);
        }
        if (configs.anisotropicFilter != 0) {
            anisotropicFilter(configs.anisotropicFilter);
        }
        borderColour(configs.borderColour);
    }

    private ITexture getTexture() {
        return texture;
    }

    /**
     * Loads a getTexture() data to the getTexture()
     *
     * @param iFormat the getTexture() internal format
     * @param width   the getTexture() width
     * @param height  the getTexture() height
     * @param format  the getTexture() data format
     * @param type    the getTexture() data type
     * @param data    the getTexture() data
     * @return this
     */
    public TextureFunctions loadTexture(FormatType iFormat, int width, int height,
                                        FormatType format, DataType type, ByteBuffer data) {
        getTexture().bind();
        GL11.glTexImage2D(target, 0, iFormat.get(), width,
                height, 0, format.get(), type.get(), data);
        return this;
    }

    /**
     * Generates the getTexture()'s mipmap
     *
     * @return this
     */
    public TextureFunctions generateMipmap() {
        getTexture().bind();
        GL30.glGenerateMipmap(target);
        return this;
    }

    /**
     * The getTexture() minifying function
     *
     * @param minFilter The param of the minifying function
     * @return this
     */
    public TextureFunctions minFilter(MinFilterParameter minFilter) {
        getTexture().bind();
        GL11.glTexParameteri(target, GL11.GL_TEXTURE_MIN_FILTER, minFilter.get());
        return this;
    }

    /**
     * The getTexture() magnification function
     *
     * @param magFilter The param of the magnification function
     * @return this
     */
    public TextureFunctions magFilter(MagFilterParameter magFilter) {
        getTexture().bind();
        GL11.glTexParameteri(target, GL11.GL_TEXTURE_MAG_FILTER, magFilter.get());
        return this;
    }

    /**
     * Sets the wrap parameter for getTexture() coordinate s (x)
     *
     * @param wrapS The param of the wrap S function
     * @return this
     */
    public TextureFunctions wrapS(WrapParameter wrapS) {
        getTexture().bind();
        GL11.glTexParameteri(target, GL11.GL_TEXTURE_WRAP_S, wrapS.get());
        return this;
    }

    /**
     * Sets the wrap parameter for getTexture() coordinate t (y)
     *
     * @param wrapT The param of the wrap T function
     * @return this
     */
    public TextureFunctions wrapT(WrapParameter wrapT) {
        getTexture().bind();
        GL11.glTexParameteri(target, GL11.GL_TEXTURE_WRAP_T, wrapT.get());
        return this;
    }

    /**
     * Sets the border colour of the getTexture()
     *
     * @param r the red   component
     * @param g the green component
     * @param b the blue  component
     * @param a the alpha component
     * @return this
     */
    public TextureFunctions borderColour(float r, float g, float b, float a) {
        getTexture().bind();
        GL11.glTexParameterfv(target, GL11.GL_TEXTURE_BORDER_COLOR, new float[]{r, g, b, a});
        return this;
    }

    public TextureFunctions borderColour(Vector4f colour) {
        return borderColour(colour.x, colour.y, colour.z, colour.w);
    }

    /**
     * Specifies a fixed bias value to be added to the LOD parameter for the getTexture() before getTexture() sampling
     *
     * @param param the fixed bias value
     * @return this
     */
    public TextureFunctions levelOfDetailBias(float param) {
        getTexture().bind();
        GL11.glTexParameterf(target, GL14.GL_TEXTURE_LOD_BIAS, param);
        return this;
    }

    /**
     * Generates anisotropic filtering
     *
     * @return this
     */
    public TextureFunctions anisotropicFilter(float param) {
        getTexture().bind();
        if (GL.getCapabilities().GL_EXT_texture_filter_anisotropic) {
            float value = Math.max(param, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
            GL11.glTexParameterf(target, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, value);
        } else {
            System.err.println("Anisotropic filtering is not supported");
        }
        return this;
    }


}

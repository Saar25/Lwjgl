package opengl.textures;

import opengl.constants.DataType;
import opengl.constants.FormatType;
import opengl.textures.parameters.MagFilterParameter;
import opengl.textures.parameters.MinFilterParameter;
import opengl.textures.parameters.WrapParameter;

import java.nio.ByteBuffer;

public class TextureBuilder {

    private TextureInfo textureInfo = null;
    private FormatType iFormat = null;
    private FormatType format = null;
    private DataType dataType = null;

    private boolean generateMipMap = false;
    private MinFilterParameter minFilter = null;
    private MagFilterParameter magFilter = null;
    private float levelOfDetailBias = 0;
    private float anisotropicFilter = 0;

    private float borderR = 0, borderG = 0;
    private float borderB = 0, borderA = 0;
    private WrapParameter wrapS = null;
    private WrapParameter wrapT = null;

    public Texture create() {
        Texture texture = Texture.create();
        TextureFunctions functions = new TextureFunctions(texture, TextureTarget.TEXTURE_2D);
        if (textureInfo != null) {
            functions.loadTexture(iFormat, textureInfo.getWidth(), textureInfo.getHeight(),
                    format, dataType, textureInfo.getData());
        }
        final TextureConfigs configs = new TextureConfigs(iFormat, format, dataType);
        configs.mipmap = generateMipMap;
        configs.minFilter = minFilter;
        configs.magFilter = magFilter;
        configs.wrapS = wrapS;
        configs.wrapT = wrapT;
        configs.borderColour.set(borderR, borderG, borderB, borderA);
        configs.levelOfDetailBias = levelOfDetailBias;
        configs.anisotropicFilter = anisotropicFilter;
        functions.apply(configs);
        return texture;
    }

    public TextureBuilder setTextureData(int width, int height, ByteBuffer data, FormatType iFormat, FormatType format, DataType dataType) {
        this.textureInfo = new TextureInfo(width, height, data);
        this.iFormat = iFormat;
        this.format = format;
        this.dataType = dataType;
        return this;
    }

    public TextureBuilder setDepthTextureData(int width, int height, ByteBuffer data, FormatType iFormat) {
        return setTextureData(width, height, data, iFormat, FormatType.DEPTH_COMPONENT, DataType.U_BYTE);
    }

    public TextureBuilder setColourTextureData(int width, int height, ByteBuffer data) {
        return setTextureData(width, height, data, FormatType.RGBA8, FormatType.RGBA, DataType.U_BYTE);
    }

    public TextureBuilder setGenerateMipMap(boolean generateMipMap) {
        this.generateMipMap = generateMipMap;
        return this;
    }

    public TextureBuilder setMinFilter(MinFilterParameter minFilter) {
        this.minFilter = minFilter;
        return this;
    }

    public TextureBuilder setMagFilter(MagFilterParameter magFilter) {
        this.magFilter = magFilter;
        return this;
    }

    public TextureBuilder setWrapS(WrapParameter wrapS) {
        this.wrapS = wrapS;
        return this;
    }

    public TextureBuilder setWrapT(WrapParameter wrapT) {
        this.wrapT = wrapT;
        return this;
    }

    public TextureBuilder setBorderColour(float borderR, float borderG, float borderB, float borderA) {
        this.borderR = borderR;
        this.borderG = borderG;
        this.borderB = borderB;
        this.borderA = borderA;
        return this;
    }

    public TextureBuilder setLevelOfDetailBias(float levelOfDetailBias) {
        this.levelOfDetailBias = levelOfDetailBias;
        return this;
    }

    public TextureBuilder setAnisotropicFilter(float anisotropicFilter) {
        this.anisotropicFilter = anisotropicFilter;
        return this;
    }

    private static class TextureData {
        private final TextureInfo textureInfo;
        private final FormatType iFormat;
        private final FormatType format;
        private final DataType dataType;

        public TextureData(TextureInfo textureInfo, FormatType iFormat, FormatType format, DataType dataType) {
            this.textureInfo = textureInfo;
            this.iFormat = iFormat;
            this.format = format;
            this.dataType = dataType;
        }

        public TextureData(int width, int height, ByteBuffer data, FormatType iFormat, FormatType format, DataType dataType) {
            this.textureInfo = new TextureInfo(width, height, data);
            this.iFormat = iFormat;
            this.format = format;
            this.dataType = dataType;
        }
    }
}

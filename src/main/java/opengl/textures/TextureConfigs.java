package opengl.textures;

import maths.joml.Vector4f;
import maths.utils.Vector4;
import opengl.constants.DataType;
import opengl.constants.FormatType;
import opengl.textures.parameters.MagFilterParameter;
import opengl.textures.parameters.MinFilterParameter;
import opengl.textures.parameters.WrapParameter;

public class TextureConfigs {

    public final FormatType internalFormat;
    public final FormatType format;
    public final DataType dataType;

    public final Vector4f borderColour = Vector4.create();
    public MinFilterParameter minFilter;
    public MagFilterParameter magFilter;
    public WrapParameter wrapS;
    public WrapParameter wrapT;
    public float levelOfDetailBias;
    public float anisotropicFilter;
    public boolean mipmap = true;

    public TextureConfigs() {
        this(FormatType.RGBA8, FormatType.RGBA, DataType.U_BYTE);
    }

    public TextureConfigs(FormatType internalFormat, FormatType format, DataType dataType) {
        this.internalFormat = internalFormat;
        this.format = format;
        this.dataType = dataType;
    }

    public TextureConfigs copy() {
        final TextureConfigs configs = new TextureConfigs(internalFormat, format, dataType);
        configs.borderColour.set(this.borderColour);
        configs.levelOfDetailBias = this.levelOfDetailBias;
        configs.anisotropicFilter = this.anisotropicFilter;
        configs.minFilter = this.minFilter;
        configs.magFilter = this.magFilter;
        configs.mipmap = this.mipmap;
        configs.wrapS = this.wrapS;
        configs.wrapT = this.wrapT;
        return configs;
    }
}

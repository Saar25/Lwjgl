package opengl.fbos.attachment;

import opengl.constants.DataType;
import opengl.constants.FormatType;
import opengl.fbos.Fbo;
import opengl.textures.Texture;
import opengl.textures.TextureConfigs;
import opengl.textures.TextureTarget;
import opengl.textures.parameters.MagFilterParameter;
import opengl.textures.parameters.MinFilterParameter;

import java.nio.ByteBuffer;

public class TextureAttachment extends AbstractAttachment implements Attachment {

    private final Texture texture;
    private final TextureConfigs configs;

    private TextureAttachment(AttachmentType type, int attachmentIndex, Texture texture, TextureConfigs configs) {
        super(type, attachmentIndex);
        this.texture = texture;
        this.configs = configs;

        getTexture().getFunctions()
                .generateMipmap().anisotropicFilter(4f)
                .magFilter(MagFilterParameter.LINEAR)
                .minFilter(MinFilterParameter.NEAREST);
    }

    public static TextureAttachment ofColour(int index) {
        return new TextureAttachment(AttachmentType.COLOUR, index, Texture.create(),
                new TextureConfigs(FormatType.RGBA8, FormatType.RGBA, DataType.U_BYTE));
    }

    public static TextureAttachment ofColour(int index, TextureConfigs configs) {
        return new TextureAttachment(AttachmentType.COLOUR, index, Texture.create(), configs);
    }

    public static TextureAttachment ofDepth(FormatType depthLevel) {
        return new TextureAttachment(AttachmentType.DEPTH, 0, Texture.create(),
                new TextureConfigs(depthLevel, FormatType.DEPTH_COMPONENT, DataType.U_BYTE));
    }

    public static TextureAttachment ofDepth(TextureConfigs configs) {
        return new TextureAttachment(AttachmentType.DEPTH, 0, Texture.create(), configs);
    }

    public static TextureAttachment ofStencil() {
        return new TextureAttachment(AttachmentType.STENCIL, 0, Texture.create(),
                new TextureConfigs(FormatType.RGBA8, FormatType.RGBA, DataType.U_BYTE));
    }

    public static TextureAttachment ofDepthStencil() {
        return new TextureAttachment(AttachmentType.DEPTH_STENCIL, 0, Texture.create(),
                new TextureConfigs(FormatType.RGBA8, FormatType.RGBA, DataType.U_BYTE));
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public void init(Fbo fbo) {
        getTexture().allocate(TextureTarget.TEXTURE_2D, 0, configs.internalFormat, fbo.getWidth(),
                fbo.getHeight(), 0, configs.format, configs.dataType, (ByteBuffer) null);
        getTexture().attachToFbo(getAttachmentPoint(), 0);
    }

    @Override
    public void delete() {
        getTexture().delete();
    }
}

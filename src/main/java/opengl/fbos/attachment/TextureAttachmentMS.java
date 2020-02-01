package opengl.fbos.attachment;

import opengl.constants.FormatType;
import opengl.fbos.Fbo;
import opengl.textures.Texture;
import opengl.textures.TextureTarget;
import opengl.textures.parameters.MagFilterParameter;
import opengl.textures.parameters.MinFilterParameter;

public class TextureAttachmentMS extends AbstractAttachment implements MultisampledAttachment {

    private final Texture texture;
    private final FormatType iFormat;
    private final int samples;

    private TextureAttachmentMS(AttachmentType type, int attachmentIndex, FormatType iFormat, int samples) {
        super(type, attachmentIndex);
        this.texture = Texture.create(TextureTarget.TEXTURE_2D_MULTISAMPLE);
        this.iFormat = iFormat;
        this.samples = samples;

        getTexture().getFunctions()
                .generateMipmap().anisotropicFilter(4f)
                .magFilter(MagFilterParameter.LINEAR)
                .minFilter(MinFilterParameter.NEAREST);
    }

    /**
     * Creates a new colour multisampled texture attachment
     *
     * @return the created depth attachment
     */
    public static TextureAttachmentMS ofColour(int index, int samples) {
        return new TextureAttachmentMS(AttachmentType.COLOUR, index, FormatType.RGBA8, samples);
    }

    /**
     * Creates a new depth multisampled texture attachment
     *
     * @return the created depth attachment
     */
    public static TextureAttachmentMS ofDepth(int samples) {
        return new TextureAttachmentMS(AttachmentType.DEPTH, 0, FormatType.DEPTH_COMPONENT, samples);
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public void init(Fbo fbo) {
        getTexture().allocateMultisample(samples, iFormat, fbo.getWidth(), fbo.getHeight());
        getTexture().attachToFbo(getAttachmentPoint(), 0);
    }

    @Override
    public void delete() {
        getTexture().delete();
    }
}

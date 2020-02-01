package opengl.fbos.attachment;

import opengl.constants.FormatType;
import opengl.fbos.Fbo;
import opengl.fbos.RenderBuffer;
import opengl.textures.Texture;

public class RenderBufferAttachmentMS extends AbstractAttachment implements MultisampledAttachment {

    private final RenderBuffer renderBuffer;
    private final FormatType iFormat;
    private final int samples;

    public RenderBufferAttachmentMS(AttachmentType type, int attachmentIndex, RenderBuffer renderBuffer, FormatType iFormat, int samples) {
        super(type, attachmentIndex);
        this.renderBuffer = renderBuffer;
        this.iFormat = iFormat;
        this.samples = samples;
    }

    /**
     * Creates a new colour multisampled render buffer attachment
     *
     * @return the created depth attachment
     */
    public static RenderBufferAttachmentMS ofColour(int index, int samples) {
        final RenderBuffer renderBuffer = RenderBuffer.create();
        return new RenderBufferAttachmentMS(AttachmentType.COLOUR,
                index, renderBuffer, FormatType.RGBA8, samples);
    }

    /**
     * Creates a new depth multisampled render buffer attachment
     *
     * @return the created depth attachment
     */
    public static RenderBufferAttachmentMS ofDepth(int samples) {
        final RenderBuffer renderBuffer = RenderBuffer.create();
        return new RenderBufferAttachmentMS(AttachmentType.DEPTH, 0,
                renderBuffer, FormatType.DEPTH_COMPONENT, samples);
    }

    public RenderBuffer getRenderBuffer() {
        return renderBuffer;
    }

    @Override
    public Texture getTexture() {
        return Texture.NONE;
    }

    @Override
    public void init(Fbo fbo) {
        getRenderBuffer().loadStorageMultisample(fbo.getWidth(), fbo.getHeight(), iFormat, samples);
        getRenderBuffer().attachToFbo(getAttachmentPoint());
    }

    @Override
    public void delete() {
        getRenderBuffer().delete();
    }
}

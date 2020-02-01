package opengl.fbos.attachment;

import opengl.constants.FormatType;
import opengl.fbos.Fbo;
import opengl.fbos.RenderBuffer;
import opengl.textures.Texture;

public class RenderBufferAttachment extends AbstractAttachment implements Attachment {

    private final RenderBuffer renderBuffer;
    private final FormatType format;

    private RenderBufferAttachment(AttachmentType type, int attachmentIndex, RenderBuffer renderBuffer, FormatType format) {
        super(type, attachmentIndex);
        this.renderBuffer = renderBuffer;
        this.format = format;
    }

    /**
     * Creates a new colour render buffer attachment
     *
     * @return the created depth attachment
     */
    public static RenderBufferAttachment ofColour(int index) {
        final RenderBuffer renderBuffer = RenderBuffer.create();
        return new RenderBufferAttachment(AttachmentType.COLOUR, index, renderBuffer, FormatType.RGBA);
    }

    /**
     * Creates a new depth render buffer attachment
     *
     * @return the created depth attachment
     */
    public static RenderBufferAttachment ofDepth() {
        final RenderBuffer renderBuffer = RenderBuffer.create();
        return new RenderBufferAttachment(AttachmentType.DEPTH, 0, renderBuffer, FormatType.DEPTH_COMPONENT);
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
        getRenderBuffer().loadStorage(fbo.getWidth(), fbo.getHeight(), format);
        getRenderBuffer().attachToFbo(getAttachmentPoint());
    }

    @Override
    public void delete() {
        getRenderBuffer().delete();
    }
}

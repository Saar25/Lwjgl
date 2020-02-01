package opengl.fbos;

import opengl.fbos.attachment.Attachment;
import opengl.textures.parameters.MagFilterParameter;
import opengl.utils.GlBuffer;
import opengl.utils.GlUtils;
import opengl.utils.MemoryUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Fbo implements IFbo {

    public static final Fbo NULL = new Fbo(0, 0, 0);
    public static final int DEPTH_ATTACHMENT = -1;

    //private static int boundFbo = 0;

    private final int id;
    private final int width;
    private final int height;

    private final List<Attachment> attachments = new ArrayList<>();
    private Attachment depthAttachment;

    private Attachment readAttachment;
    private List<Attachment> drawAttachments;

    private Fbo(int id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
    }

    public static Fbo create(int width, int height) {
        final int id = GL30.glGenFramebuffers();
        return new Fbo(id, width, height);
    }

    /**
     * Returns the depth attachment of the fbo
     *
     * @return the depth attachment
     */
    public Attachment getDepthAttachment() {
        return depthAttachment;
    }

    /**
     * Returns the colour attachments of the fbo
     *
     * @return the colour attachments
     */
    public List<Attachment> getAttachments() {
        return attachments;
    }

    /**
     * Adds an attachment to the fbo
     *
     * @param attachment the attachment to add
     */
    public void addAttachment(Attachment attachment) {
        bind();
        switch (attachment.getAttachmentType()) {
            case COLOUR: addColourAttachment(attachment);
                break;
            case DEPTH: setDepthAttachment(attachment);
                break;
            default:
                throw new IllegalArgumentException("Cannot add to an fbo attachment of type"
                        + attachment.getAttachmentType());
        }
    }

    private void addColourAttachment(Attachment attachment) {
        attachment.init(this);
        getAttachments().add(attachment);
    }

    private void setDepthAttachment(Attachment attachment) {
        if (getDepthAttachment() != null) {
            getDepthAttachment().delete();
        }
        this.depthAttachment = attachment;
        this.depthAttachment.init(this);
    }

    /**
     * Returns whether the fbo has the same dimensions as given
     *
     * @param width  the width
     * @param height the height
     * @return true if has the same size false otherwise
     */
    public boolean isSized(int width, int height) {
        return getWidth() == width && getHeight() == height;
    }

    public void blitToScreen() {
        blitFbo(ScreenFbo.getInstance());
    }

    @Override
    public void blitFbo(IFbo fbo, MagFilterParameter filter, GlBuffer... buffers) {
        bind(FboTarget.READ_FRAMEBUFFER);
        fbo.bind(FboTarget.DRAW_FRAMEBUFFER);
        GL30.glBlitFramebuffer(0, 0, getWidth(), getHeight(), 0, 0, fbo.getWidth(),
                fbo.getHeight(), GlBuffer.getValue(buffers), filter.get());
    }

    private void blitFramebuffer(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2,
                                 MagFilterParameter filter, GlBuffer... buffers) {
        GL30.glBlitFramebuffer(x1, y1, w1, h1, x2, y2, w2, h2, GlBuffer.getValue(buffers), filter.get());
    }

    /**
     * Sets the read attachments of the fbo
     *
     * @param attachment the read attachment
     */
    public void setReadAttachment(int attachment) {
        if (attachment == DEPTH_ATTACHMENT) {
            this.readAttachment = getDepthAttachment();
        } else {
            this.readAttachment = getAttachments().get(attachment);
        }
    }

    /**
     * Sets the draw attachments of the fbo
     * by default all of the attachments are draw attachments
     *
     * @param attachments the draw attachments
     */
    public void setDrawAttachments(int... attachments) {
        final List<Attachment> list = new ArrayList<>();
        for (int attachment : attachments) {
            if (attachment == DEPTH_ATTACHMENT) {
                list.add(getDepthAttachment());
            } else {
                list.add(getAttachments().get(attachment));
            }
        }
        this.drawAttachments = list;
    }

    private Attachment getReadAttachment() {
        if (getAttachments().size() == 0) {
            throw new IllegalStateException("Cannot read from Fbo " + this);
        } else if (this.readAttachment == null) {
            this.readAttachment = getAttachments().get(0);
        }
        return this.readAttachment;
    }

    private void readAttachment() {
        GL11.glReadBuffer(getReadAttachment().getAttachmentPoint());
    }

    private List<Attachment> getDrawAttachments() {
        return drawAttachments == null ? getAttachments() : drawAttachments;
    }

    private void drawAttachments() {
        if (getDrawAttachments().size() == 1) {
            GL11.glDrawBuffer(getDrawAttachments().get(0).getAttachmentPoint());
        } else if (getDrawAttachments().size() > 1) {
            final IntBuffer buffer = MemoryUtils.allocInt(getDrawAttachments().size());
            for (Attachment attachment : getDrawAttachments()) {
                buffer.put(attachment.getAttachmentPoint());
            }
            buffer.flip();
            GL20.glDrawBuffers(buffer);
            MemoryUtil.memFree(buffer);
        }
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void bind(FboTarget target) {
        GL30.glBindFramebuffer(target.get(), id);
        switch (target) {
            case DRAW_FRAMEBUFFER:
                drawAttachments();
                setViewport();
                break;
            case FRAMEBUFFER:
                setViewport();
                break;
            case READ_FRAMEBUFFER:
                readAttachment();
                break;
            default:
        }
        /*if (!GlConfigs.CACHE_STATE || boundFbo != id) {
            GL30.glBindFramebuffer(target.get(), id);
            Fbo.boundFbo = id;
        }*/
    }

    private void setViewport() {
        GlUtils.setViewport(0, 0, getWidth(), getHeight());
    }

    @Override
    public void unbind(FboTarget target) {
        GL30.glBindFramebuffer(target.get(), 0);
        /*if (!GlConfigs.CACHE_STATE || boundFbo != 0) {
            GL30.glBindFramebuffer(target.get(), 0);
            Fbo.boundFbo = 0;
        }*/
    }

    @Override
    public void delete() {
        GL30.glDeleteFramebuffers(id);

        getAttachments().forEach(Attachment::delete);
        if (getDepthAttachment() != null) {
            getDepthAttachment().delete();
        }
    }
}

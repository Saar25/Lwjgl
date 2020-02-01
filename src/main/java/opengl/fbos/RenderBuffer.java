package opengl.fbos;

import opengl.constants.FormatType;
import org.lwjgl.opengl.GL30;

public class RenderBuffer {

    private static int boundRenderBuffer = 0;

    private final int id;
    private boolean deleted;

    private RenderBuffer(int id) {
        this.id = id;
        this.bind();
    }

    public static RenderBuffer create() {
        final int id = GL30.glGenRenderbuffers();
        return new RenderBuffer(id);
    }

    public void loadStorage(int width, int height, FormatType iFormat) {
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, iFormat.get(), width, height);
    }

    public void loadStorageMultisample(int width, int height, FormatType iFormat, int samples) {
        GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER, samples, iFormat.get(), width, height);
    }

    public void attachToFbo(int attachment) {
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, attachment, GL30.GL_RENDERBUFFER, id);
    }

    public void bind() {
        if (boundRenderBuffer != id) {
            boundRenderBuffer = id;
            GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, id);
        }
    }

    public void unbind() {
        if (boundRenderBuffer != 0) {
            boundRenderBuffer = 0;
            GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
        }
    }

    public void delete() {
        if (!deleted) {
            deleted = true;
            GL30.glDeleteRenderbuffers(id);
        }
    }
}

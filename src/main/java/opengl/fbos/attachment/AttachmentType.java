package opengl.fbos.attachment;

import org.lwjgl.opengl.GL30;

public enum AttachmentType {

    COLOUR(GL30.GL_COLOR_ATTACHMENT0),
    DEPTH(GL30.GL_DEPTH_ATTACHMENT),
    STENCIL(GL30.GL_STENCIL_ATTACHMENT),
    DEPTH_STENCIL(GL30.GL_DEPTH_STENCIL_ATTACHMENT),
    ;

    private final int value;

    AttachmentType(int value) {
        this.value = value;
    }

    public int get() {
        return value;
    }
}

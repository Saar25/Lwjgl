package opengl.fbos;

import org.lwjgl.opengl.GL30;

public enum FboTarget {

    FRAMEBUFFER(GL30.GL_FRAMEBUFFER),
    DRAW_FRAMEBUFFER(GL30.GL_DRAW_FRAMEBUFFER),
    READ_FRAMEBUFFER(GL30.GL_READ_FRAMEBUFFER);

    private final int value;

    FboTarget(int value) {
        this.value = value;
    }

    public int get() {
        return value;
    }
}

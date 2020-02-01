package opengl.utils;

import org.lwjgl.opengl.GL11;

public enum DepthFunction {

    NONE(-1),

    NEVER(GL11.GL_NEVER),
    LESS(GL11.GL_LESS),
    EQUAL(GL11.GL_EQUAL),
    LEQUAL(GL11.GL_LEQUAL),
    GREATER(GL11.GL_GREATER),
    NOTEQUAL(GL11.GL_NOTEQUAL),
    GEQUAL(GL11.GL_GEQUAL),
    ALWAYS(GL11.GL_ALWAYS),
    ;

    private final int value;

    DepthFunction(int value) {
        this.value = value;
    }

    public int get() {
        return value;
    }
}

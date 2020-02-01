package opengl.utils;

import org.lwjgl.opengl.GL11;

public enum BlendFunction {

    NONE(-1, -1),

    ALPHA(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA),
    ADDITIVE(GL11.GL_SRC_ALPHA, GL11.GL_ONE),
    ;

    private final int source;
    private final int destination;

    BlendFunction(int s, int d) {
        this.source = s;
        this.destination = d;
    }

    public int getSource() {
        return source;
    }

    public int getDestination() {
        return destination;
    }
}

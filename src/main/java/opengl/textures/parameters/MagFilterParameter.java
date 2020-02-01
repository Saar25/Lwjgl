package opengl.textures.parameters;

import org.lwjgl.opengl.GL11;

public enum MagFilterParameter implements TextureParameter {

    NEAREST(GL11.GL_NEAREST),
    LINEAR(GL11.GL_LINEAR);

    private final int value;

    MagFilterParameter(int value) {
        this.value = value;
    }

    @Override
    public int get() {
        return value;
    }
}

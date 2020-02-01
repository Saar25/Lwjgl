package opengl.textures.parameters;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;

public enum WrapParameter implements TextureParameter {

    CLAMP(GL11.GL_CLAMP),
    REPEAT(GL11.GL_REPEAT),
    CLAMP_TO_EDGE(GL12.GL_CLAMP_TO_EDGE),
    CLAMP_TO_BORDER(GL13.GL_CLAMP_TO_BORDER),
    MIRRORED_REPEAT(GL14.GL_MIRRORED_REPEAT);

    private final int value;

    WrapParameter(int value) {
        this.value = value;
    }

    @Override
    public int get() {
        return value;
    }
}

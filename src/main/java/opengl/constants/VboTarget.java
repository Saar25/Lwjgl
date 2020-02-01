package opengl.constants;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL31;

public enum VboTarget {

    ARRAY_BUFFER(GL15.GL_ARRAY_BUFFER),
    ELEMENT_ARRAY_BUFFER(GL15.GL_ELEMENT_ARRAY_BUFFER),
    UNIFORM_BUFFER(GL31.GL_UNIFORM_BUFFER),
    ;

    private final int value;

    VboTarget(int value) {
        this.value = value;
    }

    public int get() {
        return value;
    }

    public static VboTarget valueOf(int id) {
        switch (id) {
            case GL15.GL_ARRAY_BUFFER: return ARRAY_BUFFER;
            case GL15.GL_ELEMENT_ARRAY_BUFFER: return ELEMENT_ARRAY_BUFFER;
            case GL31.GL_UNIFORM_BUFFER: return UNIFORM_BUFFER;
            default: return null;
        }
    }

}

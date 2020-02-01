package opengl.constants;

import org.lwjgl.opengl.GL15;

public enum VboAccess {

    READ_ONLY(GL15.GL_READ_ONLY),
    WRITE_ONLY(GL15.GL_WRITE_ONLY),
    READ_WRITE(GL15.GL_READ_WRITE),
    ;

    private final int value;

    VboAccess(int value) {
        this.value = value;
    }

    public int get() {
        return value;
    }
}

package opengl.shaders;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;

public enum ShaderType {

    VERTEX(GL20.GL_VERTEX_SHADER),
    FRAGMENT(GL20.GL_FRAGMENT_SHADER),
    GEOMETRY(GL32.GL_GEOMETRY_SHADER),
    TESS_CONTROL(GL40.GL_TESS_CONTROL_SHADER),
    TESS_EVALUATION(GL40.GL_TESS_EVALUATION_SHADER),
    ;

    private final int value;

    ShaderType(int value) {
        this.value = value;
    }

    public int get() {
        return value;
    }
}

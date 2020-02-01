package opengl.shaders;

import org.lwjgl.opengl.GL20;

import java.util.LinkedList;
import java.util.List;

public class ShadersProgram<T> {

    private static int boundProgram = 0;

    private final int id;

    private final List<UniformProperty<T>> perRenderUniforms = new LinkedList<>();
    private final List<UniformProperty<T>> perInstanceUniforms = new LinkedList<>();

    private boolean deleted = false;

    private ShadersProgram(int id, Shader... shaders) throws Exception {
        this.id = id;

        bind();
        for (Shader shader : shaders) {
            shader.init();
            shader.attach(id);
        }
        GL20.glLinkProgram(id);
        GL20.glValidateProgram(id);
        for (Shader shader : shaders) {
            shader.detach(id);
            shader.delete();
        }
    }

    public static <T> ShadersProgram<T> create(Shader vertexShader, Shader fragmentShader) throws Exception {
        final int id = GL20.glCreateProgram();
        return new ShadersProgram<>(id, vertexShader, fragmentShader);
    }

    public static <T> ShadersProgram<T> create(String vertexFile, String fragmentFile) throws Exception {
        final Shader vertexShader = Shader.createVertex(vertexFile);
        final Shader fragmentShader = Shader.createFragment(fragmentFile);
        return ShadersProgram.create(vertexShader, fragmentShader);
    }

    public void bindAttribute(int location, String name) {
        GL20.glBindAttribLocation(id, location, name);
    }

    public void addPerRenderUniform(UniformProperty<T> uniform) {
        this.bind();
        uniform.initialize(this);
        perRenderUniforms.add(uniform);
    }

    public void addPerInstanceUniform(UniformProperty<T> uniform) {
        this.bind();
        uniform.initialize(this);
        perInstanceUniforms.add(uniform);
    }

    public void updatePerRenderUniforms(RenderState<T> state) {
        for (UniformProperty<T> uniform : perRenderUniforms) {
            uniform.load(state);
        }
    }

    public void updatePerInstanceUniforms(RenderState<T> state) {
        for (UniformProperty<T> uniform : perInstanceUniforms) {
            uniform.load(state);
        }
    }

    public int getUniformLocation(String name) {
        this.bind0();
        return GL20.glGetUniformLocation(id, name);
    }

    public void bind() {
        if (boundProgram != id) {
            GL20.glUseProgram(id);
            boundProgram = id;
        }
    }

    private void bind0() {
        GL20.glUseProgram(id);
    }

    public void unbind() {
        if (boundProgram != 0) {
            GL20.glUseProgram(0);
            boundProgram = 0;
        }
    }

    public void delete() {
        if (!deleted) {
            GL20.glDeleteProgram(id);
            deleted = true;
        }
    }

}

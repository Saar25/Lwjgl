package opengl.shaders;

public interface UniformProperty<T> {

    void load(RenderState<T> state);

    void initialize(ShadersProgram<T> shadersProgram);

    default boolean valueAvailable() {
        return true;
    }
}

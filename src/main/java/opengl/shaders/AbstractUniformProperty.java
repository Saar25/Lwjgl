package opengl.shaders;

public abstract class AbstractUniformProperty<T> implements UniformProperty<T> {

    private final String name;
    private int location = -1;

    AbstractUniformProperty(String name) {
        this.name = name;
    }

    @Override
    public final void initialize(ShadersProgram<T> shadersProgram) {
        if ((location = shadersProgram.getUniformLocation(name)) == -1) {
            System.err.println("Cannot locate uniform " + name);
        }
    }

    int getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }
}

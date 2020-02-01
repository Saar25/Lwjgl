package opengl.shaders;

import org.lwjgl.opengl.GL20;

public abstract class UniformIntProperty<T> extends AbstractUniformProperty<T> {

    protected UniformIntProperty(String name) {
        super(name);
    }

    @Override
    public void load(RenderState<T> state) {
        if (valueAvailable()) {
            final int value = getUniformValue(state);
            GL20.glUniform1i(getLocation(), value);
        }
    }

    public abstract int getUniformValue(RenderState<T> state);

}

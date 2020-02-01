package opengl.shaders;

import org.lwjgl.opengl.GL20;

public abstract class UniformFloatProperty<T> extends AbstractUniformProperty<T> {

    protected UniformFloatProperty(String name) {
        super(name);
    }

    @Override
    public void load(RenderState<T> state) {
        if (valueAvailable()) {
            final float value = getUniformValue(state);
            GL20.glUniform1f(getLocation(), value);
        }
    }

    public abstract float getUniformValue(RenderState<T> state);

}

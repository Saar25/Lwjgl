package opengl.shaders;

import org.lwjgl.opengl.GL30;

public abstract class UniformUIntProperty<T>  extends AbstractUniformProperty<T> {

    protected UniformUIntProperty(String name) {
        super(name);
    }

    @Override
    public void load(RenderState<T> state) {
        if (valueAvailable()) {
            final int value = getUniformValue(state);
            GL30.glUniform1ui(getLocation(), value);
        }
    }

    public abstract int getUniformValue(RenderState<T> state);

}

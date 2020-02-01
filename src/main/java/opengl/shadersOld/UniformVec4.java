package opengl.shadersOld;

import maths.utils.Vector4;
import opengl.shaders.ShadersProgram;
import opengl.utils.GlConfigs;
import maths.joml.Vector4f;
import org.lwjgl.opengl.GL20;

public class UniformVec4 extends Uniform<Vector4f> {

    private UniformVec4(int location) {
        super(location, null, null, Vector4.create());
    }

    public static UniformVec4 create(ShadersProgram<?> shadersProgram, String uniformName) throws Exception {
        int location = getLocation(shadersProgram, uniformName);
        return new UniformVec4(location);
    }

    @Override
    public void load(Vector4f newValue) {
        load(newValue.x, newValue.y, newValue.z, newValue.w);
    }

    public void load(float x, float y, float z, float w) {
        if (GlConfigs.CACHE_STATE || !equals(x, y, z, w)) {
            GL20.glUniform4f(location, x, y, z, w);
            value.set(x, y, z, w);
        }
    }

    private boolean equals(float x, float y, float z, float w) {
        return value.x == x && value.y == y && value.z == z && value.w == w;
    }
}

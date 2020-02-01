package opengl.shadersOld;

import maths.joml.Matrix4f;
import maths.joml.Vector2f;
import maths.joml.Vector3f;
import maths.joml.Vector4f;
import maths.utils.Matrix4;
import maths.utils.Vector2;
import maths.utils.Vector3;
import maths.utils.Vector4;
import opengl.shaders.ShadersProgram;
import opengl.utils.GlConfigs;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

public class Uniform<T> {

    private static final int NO_LOCATION = -1;

    protected final int location;
    private final ValueSetter<T> setter;
    private final UniformLoader<T> loader;

    protected T value;

    protected Uniform(int location, UniformLoader<T> loader, ValueSetter<T> setter, T value) {
        this.location = location;
        this.loader = loader;
        this.setter = setter;
        this.value = value;
    }

    public static <T> Uniform<T> create(ShadersProgram<?> shadersProgram, String uniformName,
                                        UniformLoader<T> loader, ValueSetter<T> setter, T value) {
        int location = getLocation(shadersProgram, uniformName);
        return new Uniform<>(location, loader, setter, value);
    }

    public static <T> Uniform<T> create(ShadersProgram<?> shadersProgram, String uniformName,
                                        UniformLoader<T> loader, T value) {
        return Uniform.create(shadersProgram, uniformName, loader, (a, b) -> b, value);
    }

    public static <T> Uniform<T> create(UniformLoader<T> loader) {
        return new Uniform<>(NO_LOCATION, loader, null, null);
    }

    protected static int getLocation(ShadersProgram<?> shadersProgram, String uniformName) {
        int location = shadersProgram.getUniformLocation(uniformName);
        if (location < 0) {
            System.err.println("Cannot locate uniform " + uniformName);
        }
        return location;
    }

    /**
     * Creates a mat4 uniform
     *
     * @param shadersProgram the program that the uniform is located
     * @param uniformName    the name of the uniform
     * @return the created uniform
     */
    public static Uniform<Matrix4f> createMat4(ShadersProgram<?> shadersProgram, String uniformName) {
        ValueSetter<Matrix4f> setter = (a, b) -> a.set(b);
        UniformLoader<Matrix4f> loader = (l, v) -> {
            try (MemoryStack stack = MemoryStack.stackPush()) {
                GL20.glUniformMatrix4fv(l, false, v.get(stack.mallocFloat(16)));
            }
        };
        return Uniform.create(shadersProgram, uniformName, loader, setter, Matrix4.create().scale(0));
    }

    /**
     * Creates a vec4 uniform
     *
     * @param shadersProgram the program that the uniform is located
     * @param uniformName    the name of the uniform
     * @return the created uniform
     */
    public static Uniform<Vector4f> createVec4(ShadersProgram<?> shadersProgram, String uniformName) {
        ValueSetter<Vector4f> setter = (a, b) -> a.set(b);
        UniformLoader<Vector4f> loader = (l, v) -> GL20.glUniform4f(l, v.x, v.y, v.z, v.w);
        return Uniform.create(shadersProgram, uniformName, loader, setter, Vector4.create());
    }

    /**
     * Creates a vec3 uniform
     *
     * @param shadersProgram the program that the uniform is located
     * @param uniformName    the name of the uniform
     * @return the created uniform
     */
    public static Uniform<Vector3f> createVec3(ShadersProgram<?> shadersProgram, String uniformName) {
        ValueSetter<Vector3f> setter = (a, b) -> a.set(b);
        UniformLoader<Vector3f> loader = (l, v) -> GL20.glUniform3f(l, v.x, v.y, v.z);
        return Uniform.create(shadersProgram, uniformName, loader, setter, Vector3.create());
    }

    /**
     * Creates a vec2 uniform
     *
     * @param shadersProgram the program that the uniform is located
     * @param uniformName    the name of the uniform
     * @return the created uniform
     */
    public static Uniform<Vector2f> createVec2(ShadersProgram<?> shadersProgram, String uniformName) {
        ValueSetter<Vector2f> setter = (a, b) -> a.set(b);
        UniformLoader<Vector2f> loader = (l, v) -> GL20.glUniform2f(l, v.x, v.y);
        return Uniform.create(shadersProgram, uniformName, loader, setter, Vector2.create());
    }

    /**
     * Creates a float uniform
     *
     * @param shadersProgram the program that the uniform is located
     * @param uniformName    the name of the uniform
     * @return the created uniform
     */
    public static UniformFloat createFloat(ShadersProgram<?> shadersProgram, String uniformName) {
        return UniformFloat.create(shadersProgram, uniformName);
        //UniformLoader<Float> loader = GL20::glUniform1f;
        //return Uniform.init(shadersProgram, uniformName, loader, 0f);
    }

    /**
     * Creates an int uniform
     *
     * @param shadersProgram the program that the uniform is located
     * @param uniformName    the name of the uniform
     * @return the created uniform
     */
    public static UniformInt createInt(ShadersProgram<?> shadersProgram, String uniformName) {
        return UniformInt.create(shadersProgram, uniformName);
        //UniformLoader<Integer> loader = GL20::glUniform1i;
        //return Uniform.init(shadersProgram, uniformName, loader, 0);
    }

    /**
     * Creates a bool uniform
     *
     * @param shadersProgram the program that the uniform is located
     * @param uniformName    the name of the uniform
     * @return the created uniform
     */
    public static Uniform<Boolean> createBool(ShadersProgram<?> shadersProgram, String uniformName) {
        UniformLoader<Boolean> loader = (l, v) -> GL20.glUniform1i(l, v ? 1 : 0);
        return Uniform.create(shadersProgram, uniformName, loader, false);
    }

    /**
     * Loads a value to the uniform
     *
     * @param newValue the new value for the uniform
     */
    public void load(T newValue) {
        if (GlConfigs.CACHE_STATE || value == null || setter == null || !value.equals(newValue)) {
            if (setter != null) {
                value = setter.set(value, newValue);
            }
            loader.load(location, newValue);
        }
    }
}

package opengl.shaders;

import java.util.ArrayList;
import java.util.List;

public class UniformArrayProperty<T> implements UniformProperty<T> {

    private final String name;
    private final SizeSupplier sizeSupplier;
    private final List<UniformProperty<T>> uniforms;

    public UniformArrayProperty(String name, int length, SizeSupplier sizeSupplier, UniformSupplier<T> supplier) {
        this.name = name;
        this.sizeSupplier = sizeSupplier;
        this.uniforms = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            uniforms.add(supplier.createUniform(name + "[" + i + "]", i));
        }
    }

    public UniformArrayProperty(String name, int length, UniformSupplier<T> supplier) {
        this.name = name;
        this.sizeSupplier = () -> length;
        this.uniforms = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            uniforms.add(supplier.createUniform(name + "[" + i + "]", i));
        }
    }

    @Override
    public void initialize(ShadersProgram<T> shadersProgram) {
        for (UniformProperty<T> uniform : uniforms) {
            uniform.initialize(shadersProgram);
        }
    }

    @Override
    public void load(RenderState<T> state) {
        if (valueAvailable()) {
            int size = sizeSupplier.get();
            for (int i = 0; i < size; i++) {
                uniforms.get(i).load(state);
            }
        }
    }

    public String getName() {
        return name;
    }

    public interface UniformSupplier<T> {
        UniformProperty<T> createUniform(String name, int index);
    }

    public interface SizeSupplier {
        int get();
    }
}

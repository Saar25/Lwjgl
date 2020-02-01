package engine.util;

public class Pointer<T> implements Reference<T> {

    public T value;

    private Pointer(T value) {
        this.value = value;
    }

    public static <T> Pointer<T> to(T value) {
        return new Pointer<>(value);
    }

    public static <T> Pointer<T> nullptr() {
        return new Pointer<>(null);
    }

    public void free() {
        this.value = null;
    }

    public boolean exist() {
        return value != null;
    }

    @Override
    public T get() {
        return value;
    }
}

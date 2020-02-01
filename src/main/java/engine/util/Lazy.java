package engine.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Lazy<T> implements Reference<T> {

    private final Supplier<T> supplier;

    private boolean assigned;
    private T value;

    private Consumer<T> onAssign = null;

    public Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
        this.assigned = false;
        this.value = null;
    }

    public Lazy(T value) {
        this.supplier = () -> null;
        this.assigned = true;
        this.value = value;
    }

    public void onAssign(Consumer<T> consumer) {
        onAssign = onAssign == null ? consumer : onAssign.andThen(consumer);
    }

    @Override
    public T get() {
        if (!assigned) {
            reassign();
        }
        return value;
    }

    public void forget() {
        this.assigned = false;
    }

    public void reassign() {
        this.assigned = true;
        this.value = supplier.get();
        if (onAssign != null) {
            this.onAssign.accept(value);
        }
    }

    public boolean isAssigned() {
        return assigned;
    }

    public boolean ifAssigned(Consumer<T> consumer) {
        if (assigned) {
            consumer.accept(value);
            return true;
        }
        return false;
    }
}

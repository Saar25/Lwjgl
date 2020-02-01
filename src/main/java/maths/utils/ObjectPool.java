package maths.utils;

import java.util.LinkedList;
import java.util.function.Supplier;

public class ObjectPool<T> {

    private final LinkedList<T> objects;
    private final Supplier<T> supplier;

    public ObjectPool(Supplier<T> supplier) {
        this.objects = new LinkedList<>();
        this.supplier = supplier;
    }

    public T pool() {
        if (objects.size() == 0) {
            T object = supplier.get();
            objects.add(object);
            return object;
        }
        return objects.removeFirst();
    }

    public T give(T object) {
        objects.add(object);
        return object;
    }

    public T poolAndGive() {
        return give(pool());
    }
}

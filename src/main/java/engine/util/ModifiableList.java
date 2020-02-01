package engine.util;

import java.util.List;

public class ModifiableList<T> {

    private final List<T> list;

    public ModifiableList(List<T> list) {
        this.list = list;
    }

    public boolean add(T t) {
        return list.add(t);
    }

    public boolean remove(T t) {
        return list.remove(t);
    }
}

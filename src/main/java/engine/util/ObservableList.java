package engine.util;

import java.util.*;

public class ObservableList<T> implements List<T> {

    private final List<T> list;
    private final List<ChangeListener<Change>> changeListeners = new LinkedList<>();

    public ObservableList(List<T> list) {
        this.list = list;
    }

    public static <T> ObservableList<T> observableArrayList() {
        return new ObservableList<>(new ArrayList<>());
    }

    public static <T> ObservableList<T> observableLinkedList() {
        return new ObservableList<>(new LinkedList<>());
    }

    public void addListener(ChangeListener<Change> changeListener) {
        this.changeListeners.add(changeListener);
    }

    public void removeListener(ChangeListener<Change> changeListener) {
        this.changeListeners.remove(changeListener);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <E> E[] toArray(E[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(T t) {
        final Change change = new Change(this, Collections.singletonList(t), Collections.emptyList());
        changeListeners.forEach(listener -> listener.changed(change));
        return list.add(t);
    }

    @Override
    public boolean remove(Object o) {
        if (list.remove(o)) {
            @SuppressWarnings("unchecked")
            final Change change = new Change(this, Collections.emptyList(),
                    Collections.singletonList((T) o));
            for (ChangeListener<Change> listener : changeListeners) {
                listener.changed(change);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return list.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return list.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public T get(int index) {
        return list.get(index);
    }

    @Override
    public T set(int index, T element) {
        return list.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        list.add(index, element);
        final Change change = new Change(this, Collections.singletonList(element), Collections.emptyList());
        changeListeners.forEach(listener -> listener.changed(change));
    }

    @Override
    public T remove(int index) {
        final Change change = new Change(this, Collections.emptyList(), Collections.singletonList(list.get(index)));
        changeListeners.forEach(listener -> listener.changed(change));
        return list.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return list.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    @Override
    public String toString() {
        return list.toString();
    }

    public class Change {

        private final ObservableList<T> list;
        private final List<T> added;
        private final List<T> removed;

        public Change(ObservableList<T> list, List<T> added, List<T> removed) {
            this.list = list;
            this.added = added;
            this.removed = removed;
        }

        public ObservableList<T> getList() {
            return list;
        }

        public List<T> getAdded() {
            return added;
        }

        public List<T> getRemoved() {
            return removed;
        }
    }
}

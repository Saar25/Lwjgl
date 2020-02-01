package engine.util.property;

public interface ChangeListener<T> {

    /**
     * Listen to every change on the observable value
     *
     * @param observable   the observable value that changed
     * @param oldValue     the old value
     * @param currentValue the current value
     */
    void changed(Observable<T> observable, T oldValue, T currentValue);

}

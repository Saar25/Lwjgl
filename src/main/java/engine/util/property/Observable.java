package engine.util.property;

/**
 * Observable for a value, every time the value changes
 * all the listeners are invoked
 *
 * @author Saar ----
 * @version 1.3
 * @since 29.3.2019
 */
public interface Observable<T> {

    /**
     * Returns the value
     *
     * @return the value
     */
    T getValue();

    /**
     * Sets the value
     */
    void setValue(T value);

    /**
     * Add a listener to the value property, the listener will be
     * invoked every time the value changes
     *
     * @param listener the listener
     */
    void addListener(ChangeListener<T> listener);

    /**
     * Removes a listener from the value property
     *
     * @param listener the listener
     */
    void removeListener(ChangeListener<T> listener);
}

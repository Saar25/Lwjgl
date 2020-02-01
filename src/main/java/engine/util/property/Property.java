package engine.util.property;

public interface Property<T> extends Observable<T> {

    /**
     * Bind the value of this property to the value of the given property
     *
     * @param property the property to bind to
     */
    void bind(Observable<T> property);

    /**
     * Unbind the value of this property to the value of the given property
     *
     * @param property the property to unbind from
     */
    void unbind(Observable<T> property);

}

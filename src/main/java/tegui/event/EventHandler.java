package tegui.event;

public interface EventHandler<T> {

    void handle(T event);

    default EventHandler<T> andThen(EventHandler<? super T> listener) {
        return event -> { handle(event); listener.handle(event);};
    }

}

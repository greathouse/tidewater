package greenmoonsoftware.es.event;

public interface EventSubscriber<T extends Event> {
    void onEvent(T event);
}

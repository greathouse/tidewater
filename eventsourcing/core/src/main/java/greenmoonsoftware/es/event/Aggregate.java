package greenmoonsoftware.es.event;

public interface Aggregate {
    String getId();
    void apply(EventList event);
}

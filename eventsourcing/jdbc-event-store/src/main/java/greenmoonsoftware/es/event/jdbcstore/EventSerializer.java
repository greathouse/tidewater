package greenmoonsoftware.es.event.jdbcstore;

import greenmoonsoftware.es.event.Event;

import java.io.IOException;
import java.io.InputStream;

public interface EventSerializer<T extends Event> {
    InputStream serialize(T event) throws IOException;
    T deserialize(String eventType, InputStream stream) throws IOException;
}

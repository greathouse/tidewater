package greenmoonsoftware.es.event.jdbcstore;

import greenmoonsoftware.es.event.Event;

import java.io.*;

public class ObjectEventSerializer implements EventSerializer<Event> {
    @Override
    public InputStream serialize(Event event) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(event);
        byte[] bytes = baos.toByteArray();
        return  new ByteArrayInputStream(bytes);
    }

    @Override
    public Event deserialize(String eventType, InputStream stream) throws IOException {
        try {
            return (Event) new ObjectInputStream(stream).readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

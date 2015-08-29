package greenmoonsoftware.gopherwave.intellij.client;

import com.intellij.openapi.diagnostic.Logger;
import greenmoonsoftware.es.Bus;
import greenmoonsoftware.es.event.Event;
import greenmoonsoftware.es.event.SimpleEventBus;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class JavaSerializationClient {
    private final Logger log = Logger.getInstance(this.getClass());
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Bus eventBus;
    private String serverAddress;
    private int port;

    public void start(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
        connect(serverAddress, port);

        new Thread(() -> {
            while(true) {
                try {
                    Object obj = in.readObject();
                    if (eventBus != null) {
                        eventBus.post(obj);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void connect(String serverAddress, int port) {
        try {
            Socket socket = new Socket(serverAddress, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            log.error("Unable to connect to server: \""+serverAddress + "\" on port: \"" + port + "\"", e);
            throw new RuntimeException(e);
        }
    }

    public void raiseEvents(List<Event> events) {
        events.stream().forEach(this::writeEvent);
    }

    private void writeEvent(Event e) {
        System.out.println(e);
        try {
            out.writeObject(e);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Caught IOException.", ex);
        }
    }

    public void setEventBus(SimpleEventBus eventBus) {
        this.eventBus = eventBus;
    }
}
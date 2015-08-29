package greenmoonsoftware.gopherwave.server;

import greenmoonsoftware.es.Bus;
import greenmoonsoftware.es.event.Event;
import greenmoonsoftware.es.event.EventSubscriber;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

public class ClientHandler extends Thread implements EventSubscriber<EventWrapper> {
    private UUID handlerId = UUID.randomUUID();
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Bus bus;

    public ClientHandler(Socket socket, Bus bus) {
        this.socket = socket;
        this.bus = bus;
        bus.register(this);
        System.out.println("Client " + handlerId + " connected");
    }

    public void run() {
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

            while (true) {
                Object obj = in.readObject();
                bus.post(new EventWrapper((Event)obj, handlerId));
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                bus.unregister(this);
            }
            try {
                socket.close();
            } catch (IOException e) {
            }
            System.out.println("Client " + handlerId + " disconnected");
        }
    }

    @Override
    public void onEvent(EventWrapper event) {
        if (out != null && event.getHandlerId() != handlerId) {
            try {
                out.writeObject(event.getEvent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
package greenmoonsoftware.gopherwave.server;

import greenmoonsoftware.es.Bus;
import greenmoonsoftware.es.event.SimpleEventBus;
import greenmoonsoftware.es.samples.docs.Document;
import greenmoonsoftware.es.store.InMemoryEventStore;

import java.net.ServerSocket;

public class ChatServer {
    private static final int PORT = 16969;

    private static final Bus eventBus = new SimpleEventBus();
    private static final InMemoryEventStore<Document> eventStore = new InMemoryEventStore<Document>() {
        @Override
        protected Document create() {
            return new Document();
        }
    };

    public static void main(String[] args) throws Exception {
        eventBus.register(eventStore);

        System.out.println("The chat server is running.");
        try (ServerSocket listener = new ServerSocket(PORT)) {
            while (true) {
                new ClientHandler(listener.accept(), eventBus).start();
            }
        }
    }
}
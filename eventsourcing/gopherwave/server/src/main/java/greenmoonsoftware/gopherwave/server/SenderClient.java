package greenmoonsoftware.gopherwave.server;

import greenmoonsoftware.es.samples.docs.events.LinesDeletedEvent;
import greenmoonsoftware.es.samples.docs.events.LinesInsertedEvent;
import greenmoonsoftware.es.samples.docs.events.TextSelectionChangedEvent;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import java.util.stream.IntStream;

public class SenderClient {
    private final ObjectOutputStream out;
    private final Socket socket;
    private String aggregateId = "build.gradle";

    public SenderClient() throws IOException {
        String serverAddress = "localhost";
        socket = new Socket(serverAddress, 16969);
        out = new ObjectOutputStream(socket.getOutputStream());
    }

    private void run() {
        try {
            out.writeObject(selectionChanged());
            out.writeObject(inserted());
            Thread.sleep(1000);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private TextSelectionChangedEvent selectionChanged() {
        return new TextSelectionChangedEvent(aggregateId).addRange( 0, 10).addRange(20,30);
    }

    private LinesDeletedEvent deleted() {
        return new LinesDeletedEvent(aggregateId, 0, 0);
    }

    private LinesInsertedEvent inserted() {
        return new LinesInsertedEvent(aggregateId,
                0, Collections.singletonList("//" + new Date().toString() + " " + UUID.randomUUID().toString()));
    }

    public static void main(String[] args) throws Exception {
        SenderClient client = new SenderClient();
        IntStream.range(0,1).forEach(i -> client.run());
    }
}
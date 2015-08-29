package greenmoonsoftware.gopherwave.server;

import greenmoonsoftware.es.Bus;
import greenmoonsoftware.es.event.Event;
import greenmoonsoftware.es.event.EventSubscriber;
import greenmoonsoftware.es.event.SimpleEventBus;
import greenmoonsoftware.es.samples.docs.Document;
import greenmoonsoftware.es.store.InMemoryEventStore;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ChatClient implements EventSubscriber<Event> {

    ObjectInputStream in;
    ObjectOutputStream out;
    JFrame frame = new JFrame("Chatter");
    JTextField textField = new JTextField(40);
    JTextArea messageArea = new JTextArea(8, 40);

    Bus eventBus = new SimpleEventBus();
    InMemoryEventStore<Document> eventStore = new InMemoryEventStore<Document>() {
        @Override
        protected Document create() {
            return new Document();
        }
    };

    public ChatClient() {
        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.pack();

        eventBus.register(eventStore);
        eventBus.register(this);
    }

    private String getServerAddress() {
        return JOptionPane.showInputDialog(
                frame,
                "Enter IP Address of the Server:",
                "Welcome to the Chatter",
                JOptionPane.QUESTION_MESSAGE);
    }

    private String getName() {
        return JOptionPane.showInputDialog(
                frame,
                "Choose a screen name:",
                "Screen name selection",
                JOptionPane.PLAIN_MESSAGE);
    }

    private void run() throws IOException, ClassNotFoundException {
        String serverAddress = getServerAddress();
        Socket socket = new Socket(serverAddress, 9001);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

        while (true) {
            Event event = (Event)in.readObject();
            System.out.println("Before Post: "+event);
            eventBus.post(event);
        }
    }

    public static void main(String[] args) throws Exception {
        ChatClient client = new ChatClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }

    @Override
    public void onEvent(Event event) {
        System.out.println("onEvent: "+event);
        Document d = eventStore.retrieve(event.getAggregateId());
        System.out.println("------------------------\n"+d.getContent()+"\n------------------------");
        textField.setText(d.getId());
        messageArea.setText(d.getContent());
    }
}
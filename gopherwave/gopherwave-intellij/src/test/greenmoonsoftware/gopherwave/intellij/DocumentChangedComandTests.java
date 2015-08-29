package greenmoonsoftware.gopherwave.intellij;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import greenmoonsoftware.es.event.Event;
import greenmoonsoftware.es.samples.docs.events.LineChangedEvent;
import greenmoonsoftware.es.samples.docs.events.LinesDeletedEvent;
import greenmoonsoftware.es.samples.docs.events.LinesInsertedEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class DocumentChangedComandTests {
    private Document document;

    String text = "It starts with\n" +
            "One thing I don't know why\n" +
            "It doesn’t even matter how hard you try\n" +
            "Keep that in mind, I designed this rhyme\n" +
            "To explain in due time\n" +
            "All I know\n" +
            "Time is a valuable thing\n" +
            "Watch it fly by as the pendulum swings\n" +
            "Watch it count down to the end of the day\n" +
            "The clock ticks life away\n" +
            "It’s so unreal\n" +
            "Didn’t look out below\n" +
            "Watch the time go right out the window\n" +
            "Trying to hold on but didn’t even know\n" +
            "I wasted it all just to watch you go";

    @Before
    public void onSetup() {
        document = new GopherMockDocument(text);
    }

    @Test
    public void givenInsertSingleLine_shouldReturnLineChangeEvent() {
        List<Event> actual = insertString(text.indexOf("why") + 3, ", but I'm gonna find out");

        Assert.assertEquals(1, actual.size());
        Event actualEvent = actual.get(0);
        Assert.assertEquals(LineChangedEvent.class, actualEvent.getClass());
        assertLineChangedEvent(1, "One thing I don't know why, but I'm gonna find out", (LineChangedEvent) actualEvent);
    }

    @Test
    public void givenInsertedString_withMultipleLines_shouldReturnLineChangeEvent_andLinesInsertedEvent() {
        String newLine1 = "Brand new line. Inserted Event";
        String newLine2 = "Second Line";
        List<Event> actual = insertString(text.indexOf("why") + 3, " (Line changed)\n" + newLine1 + "\n" + newLine2);

        Assert.assertEquals(2, actual.size());
        LineChangedEvent lineChangedEvent = findEventsOfType(actual, LineChangedEvent.class).get(0);
        assertLineChangedEvent(1, "One thing I don't know why (Line changed)", lineChangedEvent);

        LinesInsertedEvent linesInsertedEvent = findEventsOfType(actual, LinesInsertedEvent.class).get(0);
        Assert.assertEquals(2, linesInsertedEvent.getStartLineNumber());
        Assert.assertEquals(2, linesInsertedEvent.getLines().size());
        Assert.assertEquals(newLine1, linesInsertedEvent.getLines().get(0));
        Assert.assertEquals(newLine2, linesInsertedEvent.getLines().get(1));
    }

    @Test
    public void givenReplaceString_withSingleLine_shouldReturnLineChangeEvent() {
        String replacement = "This line is totally replaced. (LineChangedEvent)";
        String lineToBeReplaced = "Keep that in mind, I designed this rhyme";
        int startIndex = text.indexOf(lineToBeReplaced);
        List<Event> actual = replaceString(startIndex, startIndex + lineToBeReplaced.length(), replacement);
        System.out.println(document.getText());

        Assert.assertEquals(1, actual.size());
        LineChangedEvent actualEvent = findEventsOfType(actual, LineChangedEvent.class).get(0);
        assertLineChangedEvent(3, replacement, actualEvent);
    }

    @Test
    public void givenReplaceString_withMultipleNewLines_shouldReturnLineChangedEvent_andLinesInsertedEvent() {
        String newLine1 = "Brand new line. (LineChangedEvent)";
        String newLine2 = "Second Line. (LinesInsertedEvent)";
        String lineToBeReplaced = "Keep that in mind, I designed this rhyme";
        int startIndex = text.indexOf(lineToBeReplaced);
        List<Event> actual = replaceString(startIndex, startIndex + lineToBeReplaced.length(), newLine1 + "\n" + newLine2);

        Assert.assertEquals(2, actual.size());
        LineChangedEvent lineChangedEvent = findEventsOfType(actual, LineChangedEvent.class).get(0);
        assertLineChangedEvent(3, newLine1, lineChangedEvent);

        LinesInsertedEvent linesInsertedEvent = findEventsOfType(actual, LinesInsertedEvent.class).get(0);
        Assert.assertEquals(4, linesInsertedEvent.getStartLineNumber());
        Assert.assertEquals(1, linesInsertedEvent.getLines().size());
        Assert.assertEquals(newLine2, linesInsertedEvent.getLines().get(0));
    }

    @Test
    public void givenReplaceString_withSameNumberOfLinesAndNewLines_shouldReturnMultipleLineChangedEvents() {
        String newLine1 = "Brand new line. (LineChangedEvent)";
        String newLine2 = "New Line 2 (LineChangedEvent)";
        String newLine3 = "New Line 3 (LineChangedEvent)";
        String linesToBeReplaced = "It doesn’t even matter how hard you try\nKeep that in mind, I designed this rhyme\nTo explain in due time";
        int startIndex = text.indexOf(linesToBeReplaced);
        List<Event> actual = replaceString(startIndex, startIndex + linesToBeReplaced.length(), String.format("%s\n%s\n%s", newLine1, newLine2, newLine3));

        Assert.assertEquals(3, actual.size());
        List<LineChangedEvent> lineChangedEvents = findEventsOfType(actual, LineChangedEvent.class);
        assertLineChangedEvent(2, newLine1, lineChangedEvents.get(0));
        assertLineChangedEvent(3, newLine2, lineChangedEvents.get(1));
        assertLineChangedEvent(4, newLine3, lineChangedEvents.get(2));
    }

    @Test
    public void givenReplacestring_withMoreOldLines_shouldReturnLineChangedEvent_andLinesDeletedEvent() {
        String newLine1 = "Brand new line. (LineChangedEvent)";
        String linesToBeReplaced = "It doesn’t even matter how hard you try\nKeep that in mind, I designed this rhyme\nTo explain in due time";
        int startIndex = text.indexOf(linesToBeReplaced);
        List<Event> actual = replaceString(startIndex, startIndex + linesToBeReplaced.length(), newLine1);

        Assert.assertEquals(2, actual.size());
        assertLineChangedEvent(2, newLine1, findEventsOfType(actual, LineChangedEvent.class).get(0));

        LinesDeletedEvent linesDeletedEvent = findEventsOfType(actual, LinesDeletedEvent.class).get(0);
        Assert.assertEquals(3, linesDeletedEvent.getStartIndex());
        Assert.assertEquals(4, linesDeletedEvent.getEndIndex());
    }

    @Test
    public void givenNewLineCharacter_shouldReturnLinesInsertedEvent() {
        List<Event> actual = insertString(text.indexOf("why") + 3, "\n");

        LinesInsertedEvent event = findEventsOfType(actual, LinesInsertedEvent.class).get(0);
        Assert.assertNotNull(event);
    }

    private <T extends Event> List<T> findEventsOfType(List<Event> events, Class<T> clazz) {
        return (List<T>)events.stream().filter(event -> event.getClass().isAssignableFrom(clazz)).collect(Collectors.toList());
    }

    private void assertLineChangedEvent(int lineNumber, String newLine, LineChangedEvent actual) {
        Assert.assertEquals(lineNumber, actual.getLineNumber());
        Assert.assertEquals(newLine, actual.getNewLine());
    }

    private List<Event> insertString(int offset, String charSequence) {
        DocumentEventCollector collector = new DocumentEventCollector();
        document.addDocumentListener(collector);
        document.insertString(offset, charSequence);
        return process(collector);
    }

    private List<Event> replaceString(int i, int length, String replacement) {
        DocumentEventCollector collector = new DocumentEventCollector();
        document.addDocumentListener(collector);
        document.replaceString(i, length, replacement);
        return process(collector);
    }

    private List<Event> process(DocumentEventCollector collector) {
//        GopherwaveDocumentListener intellijDocument = new GopherwaveDocumentListener(collector.documentChanged);
//        return intellijDocument.process("relPath", collector.documentChanged);
        return null;
    }

    private class DocumentEventCollector implements DocumentListener {
        public DocumentEvent documentChanged;

        @Override
        public void beforeDocumentChange(DocumentEvent documentEvent) {
        }

        @Override
        public void documentChanged(DocumentEvent documentEvent) {
            this.documentChanged = documentEvent;
        }
    }
}










































package greenmoonsoftware.es.samples.docs
import greenmoonsoftware.es.event.EventList
import greenmoonsoftware.es.samples.docs.events.DocumentCreatedEvent
import greenmoonsoftware.es.samples.docs.events.LineChangedEvent
import greenmoonsoftware.es.samples.docs.events.LinesDeletedEvent
import greenmoonsoftware.es.samples.docs.events.LinesInsertedEvent
import org.testng.annotations.Test

class DocumentEventTests {
    @Test
    void givenDocumentCreated_shouldCreateAnEmptyDocument() {
        def document = new Document()
        assert document.getId() == null, 'PRE-CHECK: No ID assigned yet'

        def expectedId = UUID.randomUUID().toString()
        document.apply(new EventList(new DocumentCreatedEvent(expectedId)))

        assert expectedId == document.id
    }

    @Test
    void givenLineChangedEvent_shouldBeExpectedParagraph() {
        def document = createDocument()
        def expected = 'Four score and seven years ago our fathers brought forth on this continent a new nation, conceived in liberty, and dedicated to the proposition that all men are created equal.'
        document.apply(new EventList().add(new LineChangedEvent(document.id, 1, expected)))
        assert expected == document.content
    }

    @Test
    void givenMultipleLineChangedEvents_shouldBeExpectedParagraph() {
        def document = createDocument()
        def line1 = 'Four score and seven years ago our fathers brought forth on this continent a new nation, conceived in liberty, and dedicated to the proposition that all men are created equal.'
        def line2 = 'Now we are engaged in a great civil war, testing whether that nation, or any nation so conceived and so dedicated, can long endure. We are met on a great battlefield of that war. We have come to dedicate a portion of that field, as a final resting place for those who here gave their lives that that nation might live. It is altogether fitting and proper that we should do this.'
        document.apply(new EventList().add(new LineChangedEvent(document.id, 1, line1)))
        document.apply(new EventList().add(new LineChangedEvent(document.id, 2, line2)))
        assert "${line1}\n${line2}" == document.content
    }

    @Test
    void givenMultipleLineChangedEventsForTheSameLine_shouldBeExpectedParagraph() {
        def document = createDocument()
        def line1 = 'Four score and seven years ago our fathers brought forth on this continent a new nation, conceived in liberty, and dedicated to the proposition that all men are created equal.'
        line1.inject('') {line, chr ->
            line += chr
            document.apply(new EventList().add(new LineChangedEvent(document.id, 1, line)))
            return line
        }
        assert line1 == document.content
    }

    @Test
    void givenLineChangedOutOfOrderEvents_shouldBeExpectedParagraph() {
        def document = createDocument()
        def line1 = 'Four score and seven years ago our fathers brought forth on this continent a new nation, conceived in liberty, and dedicated to the proposition that all men are created equal.'
        def line2 = 'Now we are engaged in a great civil war, testing whether that nation, or any nation so conceived and so dedicated, can long endure. We are met on a great battlefield of that war. We have come to dedicate a portion of that field, as a final resting place for those who here gave their lives that that nation might live. It is altogether fitting and proper that we should do this.'
        document.apply(new EventList().add(new LineChangedEvent(document.id, 2, line2)))
        document.apply(new EventList().add(new LineChangedEvent(document.id, 1, line1)))
        assert "${line1}\n${line2}" == document.content
    }

    @Test
    void givenLinesInsertedEvent_shouldInsertLinesAppropriately() {
        def document = createDocument()
        def line1 = 'Four score and seven years ago our fathers brought forth on this continent a new nation, conceived in liberty, and dedicated to the proposition that all men are created equal.'
        def line2 = 'Now we are engaged in a great civil war, testing whether that nation, or any nation so conceived and so dedicated, can long endure. We are met on a great battlefield of that war. We have come to dedicate a portion of that field, as a final resting place for those who here gave their lives that that nation might live. It is altogether fitting and proper that we should do this.'
        addContent(document, 1, line1, line2)

        def inserted = 'Hello'
        document.apply(new EventList(new LinesInsertedEvent(document.id, 2, [inserted])))

        assert "${line1}\n${inserted}\n${line2}" == document.content
    }

    @Test
    void givenLinesInseteredEvent_withMultipleLines_shouldInsertLinesAppropriately() {
        def document = createDocument()
        def line1 = 'Four score and seven years ago our fathers brought forth on this continent a new nation, conceived in liberty, and dedicated to the proposition that all men are created equal.'
        def line2 = 'Now we are engaged in a great civil war, testing whether that nation, or any nation so conceived and so dedicated, can long endure. We are met on a great battlefield of that war. We have come to dedicate a portion of that field, as a final resting place for those who here gave their lives that that nation might live. It is altogether fitting and proper that we should do this.'
        addContent(document, 1, line1, line2)

        def inserted1 = 'Hello'
        def inserted2 = 'Abraham Lincoln'
        document.apply(new EventList(new LinesInsertedEvent(document.id, 2, [inserted1, inserted2])))

        assert "${line1}\n${inserted1}\n${inserted2}\n${line2}" == document.content
    }

    @Test
    void givenLinesDeletedEvent_shouldBeExpectedParagraph() {
        def document = createDocument()
        def line1 = 'Four score and seven years ago our fathers brought forth on this continent a new nation, conceived in liberty, and dedicated to the proposition that all men are created equal.'
        def line2 = 'Now we are engaged in a great civil war, testing whether that nation, or any nation so conceived and so dedicated, can long endure. We are met on a great battlefield of that war. We have come to dedicate a portion of that field, as a final resting place for those who here gave their lives that that nation might live. It is altogether fitting and proper that we should do this.'
        addContent(document, 1, line1, line2)

        document.apply(new EventList().add(new LinesDeletedEvent(document.id, 1, 1)))

        assert line2 == document.content
    }

    @Test
    void givenLinesDeletedEvent_withMultipleLines_shouldBeExpectedParagraph() {
        def document = createDocument()
        def line1 = 'Four score and seven years ago our fathers brought forth on this continent a new nation, conceived in liberty, and dedicated to the proposition that all men are created equal.'
        def line2 = 'Now we are engaged in a great civil war, testing whether that nation, or any nation so conceived and so dedicated, can long endure. We are met on a great battlefield of that war. We have come to dedicate a portion of that field, as a final resting place for those who here gave their lives that that nation might live. It is altogether fitting and proper that we should do this.'
        def line3 = 'But, in a larger sense, we can not dedicate, we can not consecrate, we can not hallow this ground.'
        def line4 = 'The brave men, living and dead, who struggled here, have consecrated it, far above our poor power to add or detract.'
        addContent(document, 1, line1, line2, line3, line4)

        document.apply(new EventList().add(new LinesDeletedEvent(document.id, 2, 3)))

        assert "${line1}\n${line4}" == document.content
    }

    private void addContent(Document doc, int line, String... content) {
        (line..<line+content.size()).eachWithIndex { lineNumber, index ->
            doc.apply(new EventList().add(new LineChangedEvent(doc.id, lineNumber, content[index])))
        }
    }

    private static Document createDocument() {
        def document = new Document()
        document.apply(new EventList(new DocumentCreatedEvent(UUID.randomUUID().toString())))
        return document
    }
}

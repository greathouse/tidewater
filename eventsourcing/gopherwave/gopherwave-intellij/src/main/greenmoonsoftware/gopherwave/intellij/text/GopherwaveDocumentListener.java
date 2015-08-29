package greenmoonsoftware.gopherwave.intellij.text;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.project.Project;
import greenmoonsoftware.es.event.Event;
import greenmoonsoftware.es.samples.docs.events.LineChangedEvent;
import greenmoonsoftware.es.samples.docs.events.LinesDeletedEvent;
import greenmoonsoftware.es.samples.docs.events.LinesInsertedEvent;
import greenmoonsoftware.gopherwave.intellij.client.JavaSerializationClient;
import greenmoonsoftware.gopherwave.intellij.IFileDocumentManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GopherwaveDocumentListener implements DocumentListener {
    private final IFileDocumentManager documentManager;
    private final Project project;
    private final JavaSerializationClient service;
    private boolean isListening = true;

    public GopherwaveDocumentListener(IFileDocumentManager documentManager, Project project, JavaSerializationClient service) {
        this.documentManager = documentManager;
        this.project = project;
        this.service = service;
    }

    @Override
    public void beforeDocumentChange(DocumentEvent documentEvent) {
    }

    public void stop() {
        this.isListening = false;
    }

    public void start() {
        this.isListening = true;
    }

    @Override
    public void documentChanged(DocumentEvent documentEvent) {
        if (!isListening) {
            return;
        }
        //IntelliJ be stupid
        if (documentEvent.getNewFragment().toString().contains("IntellijIdeaRulezzz")) {
            System.out.println("IGNORED: " + documentEvent);
            return;
        }
        String relativePath = documentManager.getRelativePath(documentEvent.getDocument());
        if (relativePath == null || relativePath.isEmpty()) {
            System.out.println("IGNORED: NO VirtualFile found. " + documentEvent);
            return;
        }

        List<Event> events = process(relativePath, documentEvent);
        service.raiseEvents(events);
    }

    private List<Event> process(String relativePath, DocumentEvent event) {
        Document d = event.getDocument();
        int lineNumber = d.getLineNumber(event.getOffset());
        int numberOfOldLines = countOfLines(event.getOldFragment().toString());
        int numberOfNewLines = countOfLines(event.getNewFragment().toString());

        List<Event> events = new ArrayList<>();
        int numberOfLinesChanged = numberOfOldLines < numberOfNewLines ? numberOfOldLines : numberOfNewLines;
        events.addAll(IntStream.range(lineNumber, lineNumber + numberOfLinesChanged)
                .mapToObj(l -> new LineChangedEvent(relativePath, l, getLineFromDocument(d, l)))
                .collect(Collectors.toList()));

        if (numberOfOldLines < numberOfNewLines) {
            List<String> newLinesList = IntStream.range(lineNumber + numberOfOldLines, lineNumber + numberOfNewLines)
                    .mapToObj(l -> getLineFromDocument(d, l))
                    .collect(Collectors.toList());
            events.add(new LinesInsertedEvent(relativePath, lineNumber + numberOfOldLines, newLinesList));
        }

        if (numberOfOldLines > numberOfNewLines) {
            events.add(new LinesDeletedEvent(relativePath, lineNumber + numberOfNewLines, lineNumber + numberOfOldLines - numberOfNewLines));
        }

        return events;
    }

    private String getLineFromDocument(Document document, int lineNumber) {
        return document.getText().substring(document.getLineStartOffset(lineNumber), document.getLineEndOffset(lineNumber));
    }

    private int countOfLines(String lines) {
        return lines.length() - lines.replace("\n", "").length() + 1;
    }
}


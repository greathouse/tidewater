package greenmoonsoftware.gopherwave.intellij.text;

import com.intellij.openapi.project.Project;
import greenmoonsoftware.es.samples.docs.events.LineChangedEvent;
import greenmoonsoftware.es.samples.docs.events.LinesDeletedEvent;
import greenmoonsoftware.es.samples.docs.events.LinesInsertedEvent;
import greenmoonsoftware.gopherwave.intellij.DocumentEventSubscriber;

import java.util.stream.Collectors;

public class TextChangedEventSubscriber extends DocumentEventSubscriber {
    public TextChangedEventSubscriber(Project project, GopherwaveDocumentListener documentListener) {
        super(project, documentListener);
    }

    private void handle(LineChangedEvent event) {
        writeAction(event.getAggregateId(), doc -> doc.replaceString(
                doc.getLineStartOffset(event.getLineNumber()),
                doc.getLineEndOffset(event.getLineNumber()),
                event.getNewLine()
        ));
    }

    private void handle(LinesInsertedEvent event) {
        writeAction(event.getAggregateId(), doc -> doc.insertString(
                doc.getLineStartOffset(event.getStartLineNumber()),
                event.getLines().stream().collect(Collectors.joining("\n")) + "\n"
        ));
    }

    private void handle(LinesDeletedEvent event) {
        writeAction(event.getAggregateId(), doc -> doc.deleteString(
                doc.getLineStartOffset(event.getStartIndex()),
                doc.getLineStartOffset(event.getEndIndex() + 1)
        ));
    }
}

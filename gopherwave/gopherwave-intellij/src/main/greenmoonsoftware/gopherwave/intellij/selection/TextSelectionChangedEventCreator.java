package greenmoonsoftware.gopherwave.intellij.selection;

import com.intellij.openapi.editor.event.SelectionEvent;
import greenmoonsoftware.es.samples.docs.events.TextSelectionChangedEvent;
import greenmoonsoftware.gopherwave.intellij.IFileDocumentManager;

import java.util.stream.IntStream;

final class TextSelectionChangedEventCreator {
    private final SelectionEvent ideEvent;
    private final IFileDocumentManager documentManager;

    TextSelectionChangedEventCreator(SelectionEvent ideEvent, IFileDocumentManager documentManager) {
        this.ideEvent = ideEvent;
        this.documentManager = documentManager;
    }

    TextSelectionChangedEvent create() {
        int[] startIndexes = startIndexes(ideEvent);
        int[] endIndexes = endIndexes(ideEvent);

        String relativePath = relativePath(ideEvent);
        TextSelectionChangedEvent event = new TextSelectionChangedEvent(relativePath);
        IntStream.range(0, startIndexes.length)
                .forEach (index -> event.addRange(startIndexes[index], endIndexes[index]));
        return event;
    }

    private String relativePath(SelectionEvent selectionEvent) {
        return documentManager.getRelativePath(selectionEvent.getEditor().getDocument());
    }

    private int[] startIndexes(SelectionEvent selectionEvent) {
        return selectionEvent.getEditor().getSelectionModel().getBlockSelectionStarts();
    }

    private int[] endIndexes(SelectionEvent selectionEvent) {
        return selectionEvent.getEditor().getSelectionModel().getBlockSelectionEnds();
    }
}

package greenmoonsoftware.gopherwave.intellij.selection;

import com.intellij.openapi.editor.event.SelectionEvent;
import com.intellij.openapi.editor.event.SelectionListener;
import com.intellij.openapi.project.Project;
import greenmoonsoftware.es.samples.docs.events.TextSelectionChangedEvent;
import greenmoonsoftware.gopherwave.intellij.client.JavaSerializationClient;
import greenmoonsoftware.gopherwave.intellij.IFileDocumentManager;

import java.util.Collections;

public class GopherwaveSelectionListener implements SelectionListener {
    private final Project project;
    private final JavaSerializationClient service;
    private final IFileDocumentManager documentManager;

    public GopherwaveSelectionListener(Project project, JavaSerializationClient service, IFileDocumentManager documentManager) {
        this.project = project;
        this.service = service;
        this.documentManager = documentManager;
    }

    @Override
    public void selectionChanged(SelectionEvent selectionEvent) {
        TextSelectionChangedEvent event = new TextSelectionChangedEventCreator(selectionEvent, documentManager).create();
        service.raiseEvents(Collections.singletonList(event));
    }
}

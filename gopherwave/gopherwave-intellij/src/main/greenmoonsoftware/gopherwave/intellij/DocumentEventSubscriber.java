package greenmoonsoftware.gopherwave.intellij;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import greenmoonsoftware.es.event.Event;
import greenmoonsoftware.es.event.EventApplier;
import greenmoonsoftware.es.event.EventSubscriber;
import greenmoonsoftware.es.samples.docs.events.DocumentCreatedEvent;
import greenmoonsoftware.gopherwave.intellij.text.GopherwaveDocumentListener;

public class DocumentEventSubscriber implements EventSubscriber<Event> {
    protected final Project project;
    protected final GopherwaveDocumentListener documentListener;

    public DocumentEventSubscriber(Project project, GopherwaveDocumentListener documentListener) {
        this.project = project;
        this.documentListener = documentListener;
    }

    @Override
    public final void onEvent(Event event) {
        EventApplier.apply(this, event);
    }

    private void handle(DocumentCreatedEvent event) {
        writeAction(event.getAggregateId(), doc -> doc.setText(event.getContent()));
    }

    protected void writeAction(String aggregateId, DocumentModifier modifier) {
        VirtualFile fileByUrl = project.getBaseDir().findFileByRelativePath(aggregateId);
        if (fileByUrl != null) {
            ApplicationManager.getApplication().invokeLater(() ->
                    ApplicationManager.getApplication().runWriteAction(() ->
                        CommandProcessor.getInstance().executeCommand(
                                project,
                                new DocumentModifierRunnable(modifier, fileByUrl),
                                "name",
                                "groupId"
                        )
                    )
            );
        }
        else {
            System.out.println("WARN: No VirtualFile with URL: " + aggregateId);
        }
    }

    protected void readAction(String aggregateId, DocumentModifier modifier) {
        VirtualFile fileByUrl = project.getBaseDir().findFileByRelativePath(aggregateId);
        if (fileByUrl != null) {
            ApplicationManager.getApplication().invokeLater(() ->
                            ApplicationManager.getApplication().runReadAction(() ->
                                            CommandProcessor.getInstance().executeCommand(
                                                    project,
                                                    new DocumentModifierRunnable(modifier, fileByUrl),
                                                    "name",
                                                    "groupId"
                                            )
                            )
            );
        }
        else {
            System.out.println("WARN: No VirtualFile with URL: " + aggregateId);
        }
    }

    private class DocumentModifierRunnable implements Runnable {
        private final DocumentModifier modifier;
        private final VirtualFile virtualFile;

        private DocumentModifierRunnable(DocumentModifier modifier, VirtualFile virtualFile) {
            this.modifier = modifier;
            this.virtualFile = virtualFile;
        }

        @Override
        public void run() {
            try {
                documentListener.stop();
                modifier.run(FileDocumentManager.getInstance().getDocument(virtualFile));
            }
            finally {
                documentListener.start();
            }
        }
    }

    protected interface DocumentModifier {
        void run(Document doc);
    }
}

package greenmoonsoftware.gopherwave.intellij.selection;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.markup.*;
import com.intellij.openapi.project.Project;
import greenmoonsoftware.es.samples.docs.events.TextSelectionChangedEvent;
import greenmoonsoftware.gopherwave.intellij.DocumentEventSubscriber;
import greenmoonsoftware.gopherwave.intellij.text.GopherwaveDocumentListener;

import java.awt.*;

public class TextSelectionChangedEventSubscriber extends DocumentEventSubscriber {
    public TextSelectionChangedEventSubscriber(Project project, GopherwaveDocumentListener documentListener) {
        super(project, documentListener);
    }

    private void handle(TextSelectionChangedEvent event) {
        readAction(event.getAggregateId(), doc -> {
            Editor[] editors = EditorFactory.getInstance().getEditors(doc, project);
            Editor editor = editors[0];
            final MarkupModel markupModel = editor.getMarkupModel();
            markupModel.removeAllHighlighters();
            final TextAttributes attributes = new TextAttributes();
            attributes.setEffectType(EffectType.SEARCH_MATCH);
            attributes.setBackgroundColor(Color.GREEN);
            attributes.setForegroundColor(Color.WHITE);
            event.getRanges().stream()
                    .forEach(range -> {
                        markupModel.addRangeHighlighter(range.start, range.end, HighlighterLayer.ERROR + 100, attributes, HighlighterTargetArea.EXACT_RANGE);
                    });
        });
    }
}

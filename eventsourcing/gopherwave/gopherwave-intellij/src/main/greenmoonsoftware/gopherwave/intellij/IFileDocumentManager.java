package greenmoonsoftware.gopherwave.intellij;

import com.intellij.openapi.editor.Document;

public interface IFileDocumentManager {
    String getRelativePath(Document document);
}

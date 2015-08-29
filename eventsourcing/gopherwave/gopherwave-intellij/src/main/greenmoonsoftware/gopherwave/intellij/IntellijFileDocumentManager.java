package greenmoonsoftware.gopherwave.intellij;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;

public class IntellijFileDocumentManager implements IFileDocumentManager {
    private final Project project;

    public IntellijFileDocumentManager(Project project) {
        this.project = project;
    }

    @Override
    public String getRelativePath(Document document) {
        VirtualFile file = getFile(document);
        if (file == null) {
            System.out.println("Could not find file");
            return "";
        }
        return VfsUtilCore.getRelativePath(file, project.getBaseDir(), '/');
    }

    private VirtualFile getFile(Document document) {
        return FileDocumentManager.getInstance().getFile(document);
    }
}

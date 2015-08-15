package greenmoonsoftware.sterling.scm.git
import greenmoonsoftware.sterling.config.Context
import greenmoonsoftware.sterling.config.Step
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder

class GitClone implements Step {
    String url
    String dir

    void execute() {
        Git.cloneRepository()
            .setURI(url)
            .setDirectory(new File(Context.get().workspace, dir))
            .call()

        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = builder.setWorkTree(new File(Context.get().workspace, dir))
                .build();
        def ref = repository.getRef('master')
        println ref.objectId.name
    }
}

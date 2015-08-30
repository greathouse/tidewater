package greenmoonsoftware.tidewater.scm.git
import greenmoonsoftware.tidewater.config.Context
import greenmoonsoftware.tidewater.config.step.AbstractStep
import greenmoonsoftware.tidewater.config.step.Input
import greenmoonsoftware.tidewater.config.step.Output
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder

class GitClone extends AbstractStep {
    @Input String url
    @Input String dir = ''
    @Input String ref = 'master'

    @Output private String sha

    void execute(Context context, File metaDirectory) {
        Git.cloneRepository()
            .setURI(url)
            .setDirectory(new File(context.workspace, dir))
            .setBranch(ref)
            .call()

        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = builder.setWorkTree(new File(context.workspace, dir))
                .build();
        def ref = repository.getRef(ref)
        sha = ref.objectId.name
    }

    String getSha() { sha }
}

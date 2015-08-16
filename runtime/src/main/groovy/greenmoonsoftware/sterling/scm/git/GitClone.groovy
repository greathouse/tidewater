package greenmoonsoftware.sterling.scm.git
import greenmoonsoftware.sterling.config.Context
import greenmoonsoftware.sterling.config.Step
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder

class GitClone implements Step {
    String url
    String dir = ''
    String ref = 'master'

    private String sha

    void execute(PrintStream log, File metaDirectory) {
        Git.cloneRepository()
            .setURI(url)
            .setDirectory(new File(Context.get().workspace, dir))
            .setBranch(ref)
            .call()

        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = builder.setWorkTree(new File(Context.get().workspace, dir))
                .build();
        def ref = repository.getRef(ref)
        sha = ref.objectId.name
    }

    @Override
    Map<String, Object> getInputs() {
        [
                url: url,
                dir: dir,
                ref: ref
        ].asImmutable()
    }

    @Override
    Map<String, Object> getOutputs() {
        [sha: sha].asImmutable()
    }

    String getSha() { sha }
}

package greenmoonsoftware.tidewater.plugins.git

import greenmoonsoftware.tidewater.Context
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input
import greenmoonsoftware.tidewater.step.Output
import greenmoonsoftware.tidewater.step.StepResult
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder

class GitClone extends AbstractStep {
    @Input String url
    @Input String dir = ''
    @Input String ref = 'master'

    @Output private String sha

    StepResult execute(Context context, File metaDirectory) {
        def log = context.&log.curry(this)
        def directory = new File(context.workspace, dir)
        log "Cloning repo: $url into ${directory.absolutePath}"

        Git.cloneRepository()
            .setURI(url)
            .setDirectory(directory)
            .setBranch(ref ?: 'master')
            .call()

        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = builder.setWorkTree(directory)
                .build();
        def ref = repository.getRef(ref)
        sha = ref.objectId.name
        log "Finished cloning. Working directory at ${ref.name} ($sha)"
        return StepResult.SUCCESS
    }

    String getSha() { sha }
}

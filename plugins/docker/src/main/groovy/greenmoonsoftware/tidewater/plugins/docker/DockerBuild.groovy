package greenmoonsoftware.tidewater.plugins.docker
import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.command.BuildImageCmd
import com.github.dockerjava.api.model.BuildResponseItem
import com.github.dockerjava.core.command.BuildImageResultCallback
import greenmoonsoftware.tidewater.Context
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input
import greenmoonsoftware.tidewater.step.Output

import static greenmoonsoftware.tidewater.plugins.docker.Helper.dockerClient

class DockerBuild extends AbstractStep {
    @Input String uri = System.env['DOCKER_HOST']
    @Input String certPath = System.env['DOCKER_CERT_PATH']

    @Input String tag
    @Input String remote
    @Input String baseDir

    @Output String imageId

    @Override
    boolean execute(Context context, File stepDirectory) {
        def log = context.&log.curry(this)
        imageId = command(dockerClient(uri, certPath))
                .exec(new TidewaterLoggingResultCallback<BuildImageResultCallback, BuildResponseItem>(new BuildImageResultCallback(), log))
                .delegate
                .awaitImageId()

        return imageId
    }

    private BuildImageCmd command(DockerClient docker) {
        def cmd = docker.buildImageCmd(new File(baseDir))
        if (tag) {
            cmd.withTag(tag)
        }
        if (remote) {
            cmd.withRemote(URI.create(remote))
        }
        return cmd
    }
}

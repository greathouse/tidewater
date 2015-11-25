package greenmoonsoftware.tidewater.plugins.docker

import com.github.dockerjava.core.command.BuildImageResultCallback
import greenmoonsoftware.tidewater.Context
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input
import greenmoonsoftware.tidewater.step.Output

class DockerBuild extends AbstractStep {
    @Input String uri = System.env['DOCKER_HOST']
    @Input String certPath = System.env['DOCKER_CERT_PATH']

    @Input String tag
    @Input String remote
    @Input String baseDir

    @Output String imageId

    @Override
    boolean execute(Context context, File stepDirectory) {
        def docker = Helper.dockerClient(uri, certPath)

        def cmd = docker.buildImageCmd(new File(baseDir))
        if (tag) {
            cmd.withTag(tag)
        }
        if (remote) {
            cmd.withRemote(URI.create(remote))
        }

        imageId = cmd.exec(new BuildImageResultCallback()).awaitImageId()

        return imageId
    }
}

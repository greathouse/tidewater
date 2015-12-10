package greenmoonsoftware.tidewater.plugins.docker

import com.github.dockerjava.api.DockerClient
import greenmoonsoftware.tidewater.Context
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input
import greenmoonsoftware.tidewater.step.StepResult

class DockerTag extends AbstractStep {
    @Input String uri = System.env['DOCKER_HOST']
    @Input String certPath = System.env['DOCKER_CERT_PATH']
    @Input String imageToTag
    @Input String repository
    @Input String tag

    private Closure<Void> log
    private DockerClient docker

    @Override
    StepResult execute(Context context, File stepDirectory) {
        init(context)

        log "Attempting to tag ${imageToTag} → ${repository}:${tag}"
        docker.tagImageCmd(imageToTag, repository, tag).exec()
        log 'Successful'
        return StepResult.SUCCESS
    }

    private init(Context context) {
        log = context.&log.curry(this)
        docker = Client.dockerClient(uri, certPath)
    }
}

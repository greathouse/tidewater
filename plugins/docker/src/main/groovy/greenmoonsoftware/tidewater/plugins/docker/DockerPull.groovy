package greenmoonsoftware.tidewater.plugins.docker

import greenmoonsoftware.tidewater.Context
import greenmoonsoftware.tidewater.plugins.docker.pull.TidewaterPullImageResultCallback
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input

class DockerPull extends AbstractStep {
    @Input String image
    @Input String uri = System.env['DOCKER_HOST']
    @Input String certPath = System.env['DOCKER_CERT_PATH']

    @Override
    boolean execute(Context context, File stepDirectory) {
        def log = context.&log.curry(this)

        return Helper.dockerClient(uri, certPath).pullImageCmd(image)
                .exec(new TidewaterPullImageResultCallback(log))
                .awaitCompletion()
                .success
    }
}

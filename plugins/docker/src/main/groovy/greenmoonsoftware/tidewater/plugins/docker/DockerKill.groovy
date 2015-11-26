package greenmoonsoftware.tidewater.plugins.docker

import com.github.dockerjava.api.DockerClient
import greenmoonsoftware.tidewater.Context
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input
import greenmoonsoftware.tidewater.step.Output
import groovy.json.JsonOutput

import static greenmoonsoftware.tidewater.plugins.docker.Client.dockerClient

class DockerKill extends AbstractStep {
    @Input String uri = System.env['DOCKER_HOST']
    @Input String certPath = System.env['DOCKER_CERT_PATH']
    @Input String containerId

    @Output int exitCode = -1

    private Closure<Void> log
    private DockerClient docker

    @Override
    boolean execute(Context context, File stepDirectory) {
        init(context)
        kill(docker)
        return isStopped()
    }

    private init(Context context) {
        log = context.&log.curry(this)
        docker = dockerClient(uri, certPath)
    }

    private void kill(DockerClient docker) {
        log "Killing container: ${containerId}"
        docker.killContainerCmd(containerId).exec()
    }

    private boolean isStopped() {
        log 'Inspecting...'
        def inspect = docker.inspectContainerCmd(containerId).exec()
        exitCode = inspect.state.exitCode
        log JsonOutput.prettyPrint(JsonOutput.toJson(inspect))

        return !inspect.state.isRunning()
    }
}

package greenmoonsoftware.tidewater.plugins.docker

import com.github.dockerjava.api.DockerClient
import greenmoonsoftware.tidewater.Context
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input
import greenmoonsoftware.tidewater.step.Output
import greenmoonsoftware.tidewater.step.StepResult
import groovy.json.JsonOutput

import static greenmoonsoftware.tidewater.plugins.docker.Client.dockerClient

class DockerStop extends AbstractStep {
    @Input String containerId
    @Input String uri = System.env['DOCKER_HOST']
    @Input String certPath = System.env['DOCKER_CERT_PATH']

    @Output int exitCode = -1

    private Closure<Void> log
    private DockerClient docker

    @Override
    StepResult execute(Context context, File stepDirectory) {
        init(context)
        stop()
        return StepResult.from(isStopped())
    }

    private void init(Context context) {
        log = context.&log.curry(this)
        docker = dockerClient(uri, certPath)
    }

    private void stop() {
        log "Attempting to stop container: ${containerId}"
        docker.stopContainerCmd(containerId).exec()
    }

    private boolean isStopped() {
        log 'Inspecting...'
        def inspect = docker.inspectContainerCmd(containerId).exec()
        exitCode = inspect.state.exitCode
        log JsonOutput.prettyPrint(JsonOutput.toJson(inspect))

        return !inspect.state.running
    }
}

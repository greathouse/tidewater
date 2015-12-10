package greenmoonsoftware.tidewater.plugins.docker
import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.command.CreateContainerResponse
import com.github.dockerjava.api.model.Bind
import com.github.dockerjava.api.model.Frame
import com.github.dockerjava.api.model.Volume
import com.github.dockerjava.core.async.ResultCallbackTemplate
import com.github.dockerjava.core.command.LogContainerResultCallback
import com.github.dockerjava.core.command.WaitContainerResultCallback
import greenmoonsoftware.tidewater.Context
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input
import greenmoonsoftware.tidewater.step.Output
import greenmoonsoftware.tidewater.step.StepResult

import static Client.dockerClient

class DockerStart extends AbstractStep {
    @Input String uri = System.env['DOCKER_HOST']
    @Input String certPath = System.env['DOCKER_CERT_PATH']
    @Input String image
    @Input String[] command
    @Input Map<String,String> binds = [:]
    @Input boolean waitForCompletion = true

    @Output String containerId
    @Output int exitCode = -1

    private Closure<Void> log
    private CreateContainerResponse container
    private DockerClient docker

    @Override
    StepResult execute(Context context, File stepDirectory) {
        init(context)
        createAndStartContainer(this.docker)
        return StepResult.from(waitIfRequired())
    }

    private boolean waitIfRequired() {
        waitForCompletion ? doWait() : true
    }

    private void init(Context context) {
        log = context.&log.curry(this)
        docker = dockerClient(uri, certPath)
    }

    private boolean doWait() {
        exitCode = awaitStatusCode()
        captureContainerLogs()
        return exitCode == 0
    }

    private CreateContainerResponse createAndStartContainer(DockerClient docker) {
        container = docker.createContainerCmd(image)
                .withCmd(command)
                .withBinds(buildBinds())
                .exec()

        docker.startContainerCmd(container.id).exec()
        containerId = container.id
        return container
    }

    private Bind[] buildBinds() {
        binds.collect { k,v -> new Bind(k, new Volume(v)) }
    }

    private int awaitStatusCode() {
        docker.waitContainerCmd(container.id)
                .exec(new WaitContainerResultCallback())
                .awaitStatusCode()
    }

    private void captureContainerLogs() {
        def logCallback = new ResultCallbackTemplate<LogContainerResultCallback, Frame>() {
            @Override
            void onNext(Frame object) {
                log object.toString()
            }
        }
        docker.logContainerCmd(container.getId())
                .withStdErr(true)
                .withStdOut(true)
                .exec(logCallback)
                .awaitCompletion()
    }
}

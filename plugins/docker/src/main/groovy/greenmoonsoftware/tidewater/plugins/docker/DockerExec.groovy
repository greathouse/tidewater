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

import static Client.dockerClient

class DockerExec extends AbstractStep {
    @Input String image
    @Input String uri = System.env['DOCKER_HOST']
    @Input String certPath = System.env['DOCKER_CERT_PATH']
    @Input String[] command
    @Input Map<String,String> binds = [:]

    @Output int exitCode

    @Override
    boolean execute(Context context, File stepDirectory) {
        def log = context.&log.curry(this)
        def docker = dockerClient(uri, certPath)

        def container = createAndStartContainer(docker, context)
        exitCode = awaitStatusCode(docker, container)
        captureContainerLogs(log, docker, container)
        return exitCode == 0
    }

    private CreateContainerResponse createAndStartContainer(DockerClient docker) {
        def container = docker.createContainerCmd(image)
                .withCmd(command)
                .withBinds(buildBinds())
                .exec()

        docker.startContainerCmd(container.id).exec()
        return container
    }

    private Bind[] buildBinds() {
        binds.collect { k,v -> new Bind(k, new Volume(v)) }
    }

    private int awaitStatusCode(DockerClient docker, CreateContainerResponse container) {
        docker.waitContainerCmd(container.id)
                .exec(new WaitContainerResultCallback())
                .awaitStatusCode()
    }

    private void captureContainerLogs(log, DockerClient docker, CreateContainerResponse container) {
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

package greenmoonsoftware.tidewater.plugins.docker
import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.model.Frame
import com.github.dockerjava.core.command.LogContainerResultCallback
import greenmoonsoftware.tidewater.Context
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input

class DockerLogs extends AbstractStep {
    @Input String uri = System.env['DOCKER_HOST']
    @Input String certPath = System.env['DOCKER_CERT_PATH']
    @Input String containerId
    @Input String toFilePath

    private Closure<Void> log
    private DockerClient docker
    private File toFile

    @Override
    boolean execute(Context context, File stepDirectory) {
        init(context)
        def logCallback = new LogContainerResultCallback() {
            @Override
            void onNext(Frame object) {
                toFile.append object.payload
            }
        }
        docker.logContainerCmd(containerId)
                .withStdErr(true)
                .withStdOut(true)
                .exec(logCallback)
                .awaitCompletion()
    }

    private init(Context context) {
        log = context.&log.curry(this)
        docker = Client.dockerClient(uri, certPath)
        toFile = new File(toFilePath)
        toFile.parentFile.mkdirs()
    }
}

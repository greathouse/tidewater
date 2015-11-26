package greenmoonsoftware.tidewater.plugins.docker
import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.model.Frame
import com.github.dockerjava.core.command.LogContainerResultCallback
import greenmoonsoftware.tidewater.Context
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input
import greenmoonsoftware.tidewater.step.Output

import java.text.DecimalFormat

class DockerLogs extends AbstractStep {
    @Input String uri = System.env['DOCKER_HOST']
    @Input String certPath = System.env['DOCKER_CERT_PATH']
    @Input String containerId
    @Input String toFilePath

    @Output String totalSize = "?"

    private Closure<Void> log
    private DockerClient docker
    private File toFile
    private long sizeInBytes = 0

    @Override
    boolean execute(Context context, File stepDirectory) {
        init(context)
        log()
        totalSize = byteSizeToHuman(sizeInBytes)
        return true
    }

    private void log() {
        docker.logContainerCmd(containerId)
                .withStdErr(true)
                .withStdOut(true)
                .exec(callback())
                .awaitCompletion()
    }

    private LogContainerResultCallback callback() {
        def logCallback = new LogContainerResultCallback() {
            @Override
            void onNext(Frame object) {
                sizeInBytes += object.payload.length
                toFile.append object.payload
            }
        }
        logCallback
    }

    private init(Context context) {
        log = context.&log.curry(this)
        docker = Client.dockerClient(uri, certPath)
        toFile = new File(toFilePath)
        toFile.parentFile.mkdirs()
    }

    private String byteSizeToHuman(long size) {
        if(size <= 0) return '0'
        final String[] units = [ 'B', 'kB', 'MB', 'GB', 'TB' ] as String[]
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024))
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups]
    }
}

package greenmoonsoftware.tidewater.plugins.docker

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.command.CreateContainerResponse
import com.github.dockerjava.api.model.Bind
import com.github.dockerjava.api.model.Frame
import com.github.dockerjava.api.model.Volume
import com.github.dockerjava.core.DockerClientBuilder
import com.github.dockerjava.core.DockerClientConfig
import com.github.dockerjava.core.async.ResultCallbackTemplate
import com.github.dockerjava.core.command.LogContainerResultCallback
import com.github.dockerjava.core.command.WaitContainerResultCallback
import com.google.common.net.HostAndPort
import greenmoonsoftware.tidewater.Context
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input
import greenmoonsoftware.tidewater.step.Output

import static com.google.common.base.Optional.fromNullable
import static com.google.common.base.Strings.isNullOrEmpty
import static java.lang.System.getProperty

class DockerExec extends AbstractStep {
    static final String DEFAULT_UNIX_ENDPOINT = "unix:///var/run/docker.sock"
    static final String DEFAULT_HOST = "localhost"
    static final int DEFAULT_PORT = 2375

    @Input String image
    @Input String uri = System.env['DOCKER_HOST']
    @Input String certPath = System.env['DOCKER_CERT_PATH']
    @Input Map<String,String> binds = [:]

    @Output int exitCode

    @Override
    boolean execute(Context context, File stepDirectory) {
        def log = context.&log.curry(this)
        def docker = dockerClient()

        def container = createAndStartContainer(docker, context)
        exitCode = awaitStatusCode(docker, container)
        captureContainerLogs(log, docker, container)
        return exitCode == 0
    }

    private DockerClient dockerClient() {
        def config = DockerClientConfig.createDefaultConfigBuilder()
                .withUri(buildUri())
                .withDockerCertPath(certPath)
                .build();
        return DockerClientBuilder.getInstance(config).build()
    }

    private CreateContainerResponse createAndStartContainer(DockerClient docker, Context context) {
        def container = docker.createContainerCmd(image)
                .withCmd('jekyll', 'build')
                .withBinds(buildBinds())
                .exec()

        docker.startContainerCmd(container.id).exec()
        container
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

    private String buildUri() {
        def endpoint = fromNullable(uri).or(defaultEndpoint())
        if (endpoint.startsWith('unix://')) {
            return endpoint
        }
        else {
            def stripped = endpoint.replaceAll('.*://', '')
            def hostAndPort = HostAndPort.fromString(stripped)
            def hostText = hostAndPort.getHostText()
            def scheme = certPath == null ? 'http' : 'https'

            def port = hostAndPort.getPortOrDefault(DEFAULT_PORT)
            def address = isNullOrEmpty(hostText) ? DEFAULT_HOST : hostText
            return "$scheme://$address:$port"
        }
    }

    private static String defaultEndpoint() {
        if (getProperty("os.name").equalsIgnoreCase("linux")) {
            return DEFAULT_UNIX_ENDPOINT
        } else {
            return DEFAULT_HOST + ":" + DEFAULT_PORT
        }
    }
}

package greenmoonsoftware.tidewater.plugins.docker

import com.github.dockerjava.core.DockerClientBuilder
import com.github.dockerjava.core.DockerClientConfig
import com.google.common.net.HostAndPort
import greenmoonsoftware.tidewater.Context
import greenmoonsoftware.tidewater.plugins.docker.pull.TidewaterPullImageResultCallback
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input

import static com.google.common.base.Optional.fromNullable
import static com.google.common.base.Strings.isNullOrEmpty
import static java.lang.System.getProperty


class DockerPull extends AbstractStep {
    static final String DEFAULT_UNIX_ENDPOINT = "unix:///var/run/docker.sock"
    static final String DEFAULT_HOST = "localhost"
    static final int DEFAULT_PORT = 2375

    @Input String image
    @Input String uri = System.env['DOCKER_HOST']
    @Input String certPath = System.env['DOCKER_CERT_PATH']

    @Override
    boolean execute(Context context, File stepDirectory) {
        def log = context.&log.curry(this)

        def config = DockerClientConfig.createDefaultConfigBuilder()
                .withUri(buildUri())
                .withDockerCertPath(certPath)
                .build();
        def docker = DockerClientBuilder.getInstance(config).build()

        return docker.pullImageCmd(image)
                .exec(new TidewaterPullImageResultCallback(log))
                .awaitCompletion()
                .success
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

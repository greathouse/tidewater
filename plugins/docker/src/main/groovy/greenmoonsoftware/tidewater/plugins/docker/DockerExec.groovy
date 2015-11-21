package greenmoonsoftware.tidewater.plugins.docker

import com.github.dockerjava.api.command.CreateContainerResponse
import com.github.dockerjava.api.model.Bind
import com.github.dockerjava.api.model.Volume
import com.github.dockerjava.core.DockerClientBuilder
import com.github.dockerjava.core.DockerClientConfig
import com.google.common.net.HostAndPort
import greenmoonsoftware.tidewater.context.Context
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input

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

    @Override
    boolean execute(Context context, File stepDirectory) {
        def log = context.&log.curry(this)
        def config = DockerClientConfig.createDefaultConfigBuilder()
                .withUri(buildUri())
                .withDockerCertPath(certPath)
                .build();
        def docker = DockerClientBuilder.getInstance(config).build()

        def volume1 = new Volume('/srv/jekyll')

        def container1 = docker.createContainerCmd('jekyll/jekyll:stable').withCmd('jekyll', 'build')
//                .withName(container1Name)
                .withBinds(new Bind("${context.attributes.workspace.absolutePath}/site", volume1)).exec();

        docker.startContainerCmd(container1.id).exec()

        return true
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

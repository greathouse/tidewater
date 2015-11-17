package greenmoonsoftware.tidewater.plugins.docker

import com.google.common.net.HostAndPort
import com.spotify.docker.client.*
import com.spotify.docker.client.messages.ProgressMessage
import greenmoonsoftware.tidewater.context.Context
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input

import java.nio.file.Paths

import static com.google.common.base.Optional.fromNullable
import static com.google.common.base.Strings.isNullOrEmpty
import static com.spotify.docker.client.DefaultDockerClient.*
import static java.lang.System.getProperty

class DockerPull extends AbstractStep {
    @Input String image
    @Input String uri = System.env['DOCKER_HOST']
    @Input String certPath = System.env['DOCKER_CERT_PATH']

    @Override
    boolean execute(Context context, File stepDirectory) {
        def log = context.&log.curry(this)
        def docker = getClient()

        docker.pull(image, new ProgressHandler() {
            @Override
            void progress(ProgressMessage message) throws DockerException {
                def idMessage = message.id() ? "${message.id()}: " : ''
                log message.id(), "${idMessage}${message.status()}"
            }
        })

        return true
    }

    //Shamelessly borrowed from DefaultDockerClient
    private DockerClient getClient() throws DockerCertificateException {
        def endpoint = fromNullable(uri).or(defaultEndpoint())
        def dockerCertPath = Paths.get(fromNullable(certPath).or(defaultCertPath()))

        def builder = new Builder()

        def certs = DockerCertificates.builder().dockerCertPath(dockerCertPath).build()

        if (endpoint.startsWith('unix://')) {
            builder.uri(endpoint)
        }
        else {
            def stripped = endpoint.replaceAll('.*://', '')
            def hostAndPort = HostAndPort.fromString(stripped)
            def hostText = hostAndPort.getHostText()
            def scheme = certs.isPresent() ? 'https' : 'http'

            def port = hostAndPort.getPortOrDefault(DEFAULT_PORT)
            def address = isNullOrEmpty(hostText) ? DEFAULT_HOST : hostText

            builder.uri("$scheme://$address:$port")
        }

        if (certs.isPresent()) {
            builder.dockerCertificates(certs.get())
        }

        return builder.build()
    }

    private static String defaultEndpoint() {
        if (getProperty("os.name").equalsIgnoreCase("linux")) {
            return DEFAULT_UNIX_ENDPOINT
        } else {
            return DEFAULT_HOST + ":" + DEFAULT_PORT
        }
    }

    private static String defaultCertPath() {
        return Paths.get(getProperty("user.home"), ".docker").toString()
    }
}

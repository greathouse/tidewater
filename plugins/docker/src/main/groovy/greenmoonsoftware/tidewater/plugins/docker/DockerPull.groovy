package greenmoonsoftware.tidewater.plugins.docker

import com.google.common.base.Optional
import com.google.common.net.HostAndPort
import com.spotify.docker.client.DefaultDockerClient
import com.spotify.docker.client.DockerCertificateException
import com.spotify.docker.client.DockerCertificates
import com.spotify.docker.client.DockerClient
import com.spotify.docker.client.DockerException
import com.spotify.docker.client.ProgressHandler
import com.spotify.docker.client.messages.ProgressMessage
import greenmoonsoftware.tidewater.context.Context
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input

import java.nio.file.Path
import java.nio.file.Paths

import static com.google.common.base.Optional.fromNullable
import static com.google.common.base.Optional.fromNullable
import static com.google.common.base.Strings.isNullOrEmpty
import static com.spotify.docker.client.DefaultDockerClient.*
import static java.lang.System.getProperty
import static java.lang.System.getenv
import static java.lang.System.getenv

class DockerPull extends AbstractStep {
    @Input String image
    @Input String uri = System.env['DOCKER_HOST']
    @Input String certPath = System.env['DOCKER_CERT_PATH']

    @Override
    boolean execute(Context context, File stepDirectory) {
        def log = context.&log.curry(this)
        def docker = getClient(log)

        docker.pull(image, new ProgressHandler() {
            @Override
            void progress(ProgressMessage message) throws DockerException {
                def id = message.id() ? "${message.id()}: " : ''
                log "${id}${message.status()}"
            }
        })

        return true
    }

    //Shamelessly borrowed from DefaultDockerClient
    private DockerClient getClient(log) throws DockerCertificateException {
        final String endpoint = fromNullable(uri).or(defaultEndpoint())
        final Path dockerCertPath = Paths.get(fromNullable(certPath)
                .or(defaultCertPath()))

        final Builder builder = new Builder()

        final Optional<DockerCertificates> certs = DockerCertificates.builder()
                .dockerCertPath(dockerCertPath).build()

        if (endpoint.startsWith('unix://')) {
            builder.uri(endpoint)
        } else {
            final String stripped = endpoint.replaceAll('.*://', '')
            final HostAndPort hostAndPort = HostAndPort.fromString(stripped)
            final String hostText = hostAndPort.getHostText()
            final String scheme = certs.isPresent() ? 'https' : 'http'

            final int port = hostAndPort.getPortOrDefault(DEFAULT_PORT)
            final String address = isNullOrEmpty(hostText) ? DEFAULT_HOST : hostText

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

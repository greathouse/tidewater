package greenmoonsoftware.tidewater.plugins.docker

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.core.DockerClientBuilder
import com.github.dockerjava.core.DockerClientConfig
import com.google.common.net.HostAndPort

import static com.google.common.base.Optional.fromNullable
import static com.google.common.base.Strings.isNullOrEmpty
import static java.lang.System.getProperty

class Client {
    static final String DEFAULT_UNIX_ENDPOINT = "unix:///var/run/docker.sock"
    static final String DEFAULT_HOST = "localhost"
    static final int DEFAULT_PORT = 2375

    static DockerClient dockerClient(String uri, String certPath) {
        def config = DockerClientConfig.createDefaultConfigBuilder()
                .withUri(buildUri(uri, certPath))
                .withDockerCertPath(certPath)
                .build();
        return DockerClientBuilder.getInstance(config).build()
    }

    private static String buildUri(String uri, String certPath) {
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

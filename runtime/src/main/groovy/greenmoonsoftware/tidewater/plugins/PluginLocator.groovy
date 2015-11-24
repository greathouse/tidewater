package greenmoonsoftware.tidewater.plugins

import greenmoonsoftware.tidewater.Tidewater
import greenmoonsoftware.tidewater.step.Step

import java.util.jar.JarFile

class PluginLocator {
    private final Map<String, URL> cache = [:]

    URL locate(Step s) {
        locate(s.class.canonicalName)
    }

    URL locate(String typeString) {
        cache[typeString] ?: find(typeString)
    }

    private URL find(String typeString) {
        def jarFile = new File(Tidewater.PLUGIN_DIR)
            .listFiles()
            .find { file ->
                new JarFile(file).getJarEntry(typeString.replace(".","/") + ".class")
            }
        def url = jarFile.toURI().toURL()
        cache[typeString] = url
        return url
    }

    static void main(String[] args) {
        println new PluginLocator().locate('greenmoonsoftware.tidewater.plugins.docker.DockerPull')
    }
}

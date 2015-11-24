package greenmoonsoftware.tidewater.plugins
import greenmoonsoftware.tidewater.Tidewater

import java.util.jar.JarFile

class PluginLocator {
    URL locate(String typeString) {
        new File(Tidewater.PLUGIN_DIR)
            .listFiles()
            .find { file ->
                new JarFile(file).getJarEntry(typeString.replace(".","/") + ".class")
            }
            .toURI()
            .toURL()
    }

    static void main(String[] args) {
        println new PluginLocator().locate('greenmoonsoftware.tidewater.plugins.docker.DockerPull')
    }
}

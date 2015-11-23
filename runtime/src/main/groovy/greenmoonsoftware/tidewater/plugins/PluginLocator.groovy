package greenmoonsoftware.tidewater.plugins

import greenmoonsoftware.tidewater.Tidewater
import greenmoonsoftware.tidewater.step.Step

import java.util.jar.JarFile

class PluginLocator {
    private final Map<Class<Step>, URL> cache = [:]

    URL locate(Step s) {
        cache[s.class] ?: find(s)
    }

    private URL find(Step s) {
        def jarFile = new File(Tidewater.PLUGIN_DIR)
            .listFiles()
            .find {
                new JarFile(it).getJarEntry(s.class.canonicalName.replace(".","/") + ".class")
            }
        def url = jarFile.toURI().toURL()
        cache[s.class] = url
        return url
    }

    static void main(String[] args) {
        def pluginClassloader = new PluginClassLoader()
        def clazz = pluginClassloader.loadClass('greenmoonsoftware.tidewater.plugins.aws.s3.S3CopyBucket')
        def instance = clazz.newInstance()
        println new PluginLocator().find(instance)
    }
}

package greenmoonsoftware.tidewater.plugins

import greenmoonsoftware.tidewater.Tidewater

//https://dzone.com/articles/java-classloader-handling
public class PluginClassLoader extends ClassLoader {
    private final ChildClassLoader childClassLoader

    public PluginClassLoader() {
        super(Thread.currentThread().getContextClassLoader())
        def uris = new File(Tidewater.PLUGIN_DIR)
                .listFiles()
                .collect { it.toURI().toURL() } as URL[]
        childClassLoader = new ChildClassLoader(uris, new DetectClass(this.getParent()))
    }

    public PluginClassLoader(URL... pluginLocations) {
        super(Thread.currentThread().getContextClassLoader())
        childClassLoader = new ChildClassLoader(pluginLocations, new DetectClass(this.getParent()))
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        try {
            return childClassLoader.findClass(name)
        }
        catch (ClassNotFoundException e) {
            return super.loadClass(name, resolve)
        }
    }

    private static class ChildClassLoader extends URLClassLoader {
        private final DetectClass realParent

        public ChildClassLoader(URL[] urls, DetectClass realParent) {
            super(urls, (ClassLoader) null)
            this.realParent = realParent
        }

        @Override
        public Class<?> findClass(String name) throws ClassNotFoundException {
            try {
                def loaded = super.findLoadedClass(name)
                return loaded ?: super.findClass(name)
            }
            catch (ClassNotFoundException e) {
                return realParent.loadClass(name)
            }
        }
    }

    private static class DetectClass extends ClassLoader {
        public DetectClass(ClassLoader parent) {
            super(parent)
        }

        @Override
        public Class<?> findClass(String name) throws ClassNotFoundException {
            return super.findClass(name)
        }
    }
}
package greenmoonsoftware.tidewater

public class PluginClassLoader extends ClassLoader {
    private ChildClassLoader childClassLoader;

    public PluginClassLoader() {
        super(Thread.currentThread().getContextClassLoader());
        def uris = new File('/Users/robert/projects/tidewater/web/build/plugins')
                .listFiles()
                .collect { it.toURI().toURL() } as URL[]
        childClassLoader = new ChildClassLoader(uris, new DetectClass(this.getParent()));
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        try {
            return childClassLoader.findClass(name);
        }
        catch (ClassNotFoundException e) {
            return super.loadClass(name, resolve);
        }
    }

    private static class ChildClassLoader extends URLClassLoader {
        private DetectClass realParent;

        public ChildClassLoader(URL[] urls, DetectClass realParent) {
            super(urls, (ClassLoader) null);
            this.realParent = realParent;
        }

        @Override
        public Class<?> findClass(String name) throws ClassNotFoundException {
            try {
                Class<?> loaded = super.findLoadedClass(name);
                if (loaded != null)
                    return loaded;
                return super.findClass(name);
            }
            catch (ClassNotFoundException e) {
                return realParent.loadClass(name);
            }
        }
    }

    private static class DetectClass extends ClassLoader {
        public DetectClass(ClassLoader parent) {
            super(parent);
        }

        @Override
        public Class<?> findClass(String name) throws ClassNotFoundException {
            return super.findClass(name);
        }
    }
}
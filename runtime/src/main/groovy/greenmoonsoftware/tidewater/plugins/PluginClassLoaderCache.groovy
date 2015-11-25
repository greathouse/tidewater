package greenmoonsoftware.tidewater.plugins

final class PluginClassLoaderCache {
    private PluginClassLoaderCache(){}

    private static final Map<String, ClassLoader> cacheByPluginType = [:]
    private static final Map<URL, ClassLoader> cacheByPluginUrl = [:]

    static ClassLoader getFor(Class c) {
        return getFor(c.canonicalName)
    }

    static ClassLoader getFor(String typeString) {
        cacheByPluginType[typeString] ?:
                locate(typeString)
    }

    static void clear() {
        cacheByPluginType.clear()
        cacheByPluginUrl.clear()
    }

    private static ClassLoader locate(String typeString) {
        return locateInApplication(typeString) ?:
                locateInPlugin(typeString)
    }

    private static ClassLoader locateInPlugin(String typeString) {
        def url = new PluginLocator().locate(typeString)
        return cacheByPluginUrl[url] ?: newClassLoader(typeString, url)
    }

    private static ClassLoader locateInApplication(String typeString) {
        try {
            def classLoader = Thread.currentThread().contextClassLoader
            Class.forName(typeString, false, classLoader)
            cacheByPluginType[typeString] = classLoader
            return classLoader
        } catch (ClassNotFoundException e) {
            return null
        }
    }

    private static ClassLoader newClassLoader(String typeString, URL pluginUrl) {
        def classLoader = new PluginClassLoader(pluginUrl)
        cacheByPluginType[typeString] = classLoader
        cacheByPluginUrl[pluginUrl] = classLoader
        return classLoader
    }
}

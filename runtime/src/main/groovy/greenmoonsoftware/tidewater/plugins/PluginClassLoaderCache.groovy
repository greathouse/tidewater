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
        def url = new PluginLocator().locate(typeString)
        return cacheByPluginUrl[url] ?: newClassLoader(typeString, url)
    }

    private static ClassLoader newClassLoader(String typeString, URL pluginUrl) {
        def classLoader = new PluginClassLoader(pluginUrl)
        cacheByPluginType[typeString] = classLoader
        cacheByPluginUrl[pluginUrl] = classLoader
        return classLoader
    }
}

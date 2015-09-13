package greenmoonsoftware.tidewater.config

class ParameterResolver {
    private List<String> parameterNames
    private final Properties system

    ParameterResolver(List params) {
        parameterNames = params
        system = System.properties
    }

    Map resolve() {
        parameterNames.collectEntries {
            ["$it" : find(it)]
        }
    }

    private String find(String name) {
        def v = system[name] ?: System.env[name]
        if (!v) {
            throw new RuntimeException("Unable to find parameter \"${name}\" in system properties or environment parameters.")
        }
        return v
    }
}

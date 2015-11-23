package greenmoonsoftware.tidewater

class Tidewater {
    static final String WORKSPACE_ROOT = System.env['TIDEWATER_HOME'] ?: "${System.properties['user.home']}/.tidewater"
    static final String PLUGIN_DIR = System.env['TIDEWATER_PLUGINS'] ?: "${WORKSPACE_ROOT}/plugins"
}

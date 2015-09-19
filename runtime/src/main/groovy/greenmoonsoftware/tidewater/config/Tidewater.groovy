package greenmoonsoftware.tidewater.config

class Tidewater {
    static final String WORKSPACE_ROOT = "${System.env['TIDEWATER_HOME']}" ?: "${System.properties['user.home']}/.tidewater"
}

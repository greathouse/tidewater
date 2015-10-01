package greenmoonsoftware.tidewater.context

final class ContextAttributes implements Serializable {
    private final ContextId id
    private String script
    private final File workspace
    private final File metaDirectory

    ContextAttributes(ContextId id) {
        this.id = id
        workspace = new File("${Tidewater.WORKSPACE_ROOT}/$id")
        metaDirectory = new File(workspace, '.meta')
    }

    ContextId getId() {
        id
    }

    File getWorkspace() {
        workspace
    }

    File getMetaDirectory() {
        metaDirectory
    }

    String getScript() {
        script
    }

    void setScript(String script) {
        if (this.script) {
            throw new RuntimeException('Script is already set. Cannot change.')
        }
        this.script = script
    }
}

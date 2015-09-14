package greenmoonsoftware.tidewater.web.scripts.list

class ScriptMetaData {
    String name
    String text

    ScriptMetaData(File script) {
        name = removeExtension(script.name)
        text = script.text
    }

    private String removeExtension(String filename) {
        int indexOfExtension = indexOfExtension(filename)
        return (indexOfExtension == -1) ? filename : filename.substring(0, indexOfExtension)
    }

    private static final char EXTENSION_SEPARATOR = '.'
    private static final char DIRECTORY_SEPARATOR = '/'

    private int indexOfExtension(String filename) {
        if (filename == null) {
            return -1
        }
        // Check that no directory separator appears after the
        // EXTENSION_SEPARATOR
        int extensionPos = filename.lastIndexOf(EXTENSION_SEPARATOR as int)
        int lastDirSeparator = filename.lastIndexOf(DIRECTORY_SEPARATOR as int)
        return (lastDirSeparator > extensionPos) ? -1 : extensionPos
    }
}

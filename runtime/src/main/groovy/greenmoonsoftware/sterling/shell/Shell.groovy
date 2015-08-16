package greenmoonsoftware.sterling.shell

import greenmoonsoftware.sterling.config.Context
import greenmoonsoftware.sterling.config.Step

class Shell implements Step {
    String contents
    private File workingDir = Context.get().workspace

    @Override
    void execute(PrintStream log, File metaDirectory) {
        metaDirectory.mkdirs()
        def scriptFile = new File(metaDirectory, 'script.sh')
        scriptFile.withWriter {
            it.println(contents)
        }

        ProcessBuilder builder = new ProcessBuilder('sh', scriptFile.absolutePath)
        builder.directory(workingDir)
        builder.redirectErrorStream(true)
        Process process = builder.start()
        BufferedReader reader = new BufferedReader (new InputStreamReader(process.inputStream))
        String line
        while ((line = reader.readLine ()) != null) {
            log.println(line)
        }
    }

    @Override
    Map<String, Object> getInputs() {
        [
                workingDir: workingDir,
                contents: contents
        ].asImmutable()
    }

    @Override
    Map<String, Object> getOutputs() {
        return null
    }

    void setWorkingDir(String s) {
        workingDir = new File(Context.get().workspace, s)
    }
}

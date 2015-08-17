package greenmoonsoftware.tidewater.shell

import greenmoonsoftware.tidewater.config.Context
import greenmoonsoftware.tidewater.config.step.AbstractStep
import greenmoonsoftware.tidewater.config.step.Input

class Shell extends AbstractStep {
    @Input String contents
    @Input private File workingDir = Context.get().workspace

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

    void setWorkingDir(String s) {
        workingDir = new File(Context.get().workspace, s)
    }
}

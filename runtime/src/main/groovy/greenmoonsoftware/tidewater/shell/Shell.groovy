package greenmoonsoftware.tidewater.shell

import greenmoonsoftware.tidewater.config.Context
import greenmoonsoftware.tidewater.config.step.AbstractStep
import greenmoonsoftware.tidewater.config.step.Input

class Shell extends AbstractStep {
    @Input String contents
    @Input private File workingDir = Context.get().workspace

    @Override
    void execute(PrintStream log, File metaDirectory) {
        executeProcess(buildProcess(writeScript(metaDirectory))).eachLine {
            log.println(it)
        }
    }

    private BufferedReader executeProcess(ProcessBuilder builder) {
        new BufferedReader(new InputStreamReader(builder.start().inputStream))
    }

    private ProcessBuilder buildProcess(File scriptFile) {
        new ProcessBuilder('sh', scriptFile.absolutePath)
        .with {
            directory = workingDir
            redirectErrorStream(true)
            delegate
        } as ProcessBuilder
    }

    private File writeScript(File metaDirectory) {
        metaDirectory.mkdirs()
        def scriptFile = new File(metaDirectory, 'script.sh')
        scriptFile.withWriter {
            it.println(contents)
        }
        return scriptFile
    }

    void setWorkingDir(String s) {
        workingDir = new File(Context.get().workspace, s)
    }
}
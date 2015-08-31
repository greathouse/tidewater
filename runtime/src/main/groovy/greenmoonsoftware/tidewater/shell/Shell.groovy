package greenmoonsoftware.tidewater.shell
import greenmoonsoftware.tidewater.config.Context
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input
import greenmoonsoftware.tidewater.step.StepLogEvent

class Shell extends AbstractStep {
    @Input String contents
    @Input String workingDir = ''

    @Override
    void execute(Context context, File stepDirectory) {
        executeProcess(buildProcess(context, writeScript(stepDirectory))).eachLine {
            context.raiseEvent(new StepLogEvent(this, it))
        }
    }

    private BufferedReader executeProcess(ProcessBuilder builder) {
        new BufferedReader(new InputStreamReader(builder.start().inputStream))
    }

    private ProcessBuilder buildProcess(Context context, File scriptFile) {
        new ProcessBuilder('sh', scriptFile.absolutePath)
        .with {
            directory = new File(context.workspace, workingDir)
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
}

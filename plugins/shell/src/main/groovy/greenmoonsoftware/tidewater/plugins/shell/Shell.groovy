package greenmoonsoftware.tidewater.plugins.shell
import greenmoonsoftware.tidewater.config.Context
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input

class Shell extends AbstractStep {
    @Input String contents
    @Input String workingDir = ''
    @Input Map<String, String> env = [:]

    @Override
    boolean execute(Context context, File stepDirectory) {
        executeProcess(buildProcess(context, writeScript(stepDirectory))).eachLine {
            context.log(this, it)
        }
        return true
    }

    private BufferedReader executeProcess(ProcessBuilder builder) {
        new BufferedReader(new InputStreamReader(builder.start().inputStream))
    }

    private ProcessBuilder buildProcess(Context context, File scriptFile) {
        new ProcessBuilder('sh', scriptFile.absolutePath)
        .with {
            directory(new File(context.workspace, workingDir))
            redirectErrorStream(true)
            env.each { environment().put(it.key, it.value) }
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

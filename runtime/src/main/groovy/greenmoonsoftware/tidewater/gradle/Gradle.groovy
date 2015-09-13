package greenmoonsoftware.tidewater.gradle
import greenmoonsoftware.tidewater.config.Context
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input
import greenmoonsoftware.tidewater.step.events.StepLogEvent
import groovy.transform.ToString

@ToString
class Gradle extends AbstractStep {
    @Input String workingDir = ''
    @Input String executable = './gradlew'
    @Input String buildFile = 'build.gradle'
    @Input String tasks = 'clean'

    @Override
    boolean execute(Context c, File metaDirectory) {
        return executeProcess(buildProcess(c)) {
            c.raiseEvent(new StepLogEvent(this, it))
        }
    }

    private boolean executeProcess(ProcessBuilder builder, Closure eachLine) {
        def process = builder.start()
        new BufferedReader(new InputStreamReader(process.inputStream)).eachLine eachLine
        return process.exitValue() == 0
    }

    private ProcessBuilder buildProcess(Context context) {
        new ProcessBuilder("${executable} -b ${buildFile} ${tasks}".split(' ')).with {
            directory new File(context.attributes.workspace, workingDir)
            redirectErrorStream(true)
            delegate
        } as ProcessBuilder
    }
}

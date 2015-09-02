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
    void execute(Context c, File metaDirectory) {
        executeProcess(buildProcess(c)).eachLine {
            c.raiseEvent(new StepLogEvent(this, it))
        }
    }

    private BufferedReader executeProcess(ProcessBuilder builder) {
        new BufferedReader(new InputStreamReader(builder.start().inputStream))
    }

    private ProcessBuilder buildProcess(Context context) {
        new ProcessBuilder("${executable} -b ${buildFile} ${tasks}".split(' ')).with {
            directory = new File(context.workspace, workingDir)
            redirectErrorStream(true)
            delegate
        } as ProcessBuilder
    }
}

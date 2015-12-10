package greenmoonsoftware.tidewater.plugins.gradle
import greenmoonsoftware.tidewater.Context
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input
import greenmoonsoftware.tidewater.step.StepResult
import groovy.transform.ToString

@ToString
class Gradle extends AbstractStep {
    @Input String workingDir = ''
    @Input String executable = './gradlew'
    @Input String buildFile = 'build.gradle'
    @Input String tasks = 'clean'

    @Override
    StepResult execute(Context c, File metaDirectory) {
        StepResult.from(executeProcess(buildProcess(c)) {
            c.log(this, it)
        })
    }

    private boolean executeProcess(ProcessBuilder builder, Closure eachLine) {
        def process = builder.start()
        new BufferedReader(new InputStreamReader(process.inputStream)).eachLine eachLine
        process.waitFor()
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

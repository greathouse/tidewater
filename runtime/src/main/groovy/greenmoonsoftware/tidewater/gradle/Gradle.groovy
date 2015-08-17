package greenmoonsoftware.tidewater.gradle

import greenmoonsoftware.tidewater.config.Context
import greenmoonsoftware.tidewater.config.step.AbstractStep
import greenmoonsoftware.tidewater.config.step.Input
import groovy.transform.ToString

@ToString
class Gradle extends AbstractStep {
    @Input private File workingDir = Context.get().workspace
    @Input String executable = './gradlew'
    @Input String buildFile = 'build.gradle'
    @Input String tasks = 'clean'

    @Override
    void execute(PrintStream log, File metaDirectory) {
        executeProcess(buildProcess()).eachLine {
            log.println(it)
        }
    }

    private BufferedReader executeProcess(ProcessBuilder builder) {
        new BufferedReader(new InputStreamReader(builder.start().inputStream))
    }

    private ProcessBuilder buildProcess() {
        new ProcessBuilder("${executable} -b ${buildFile} ${tasks}".split(' ')).with {
            directory = workingDir
            redirectErrorStream(true)
            delegate
        } as ProcessBuilder
    }

    File getWorkingDir() {
        workingDir
    }

    void setWorkingDir(String s) {
        workingDir = new File(Context.get().workspace, s)
    }
}

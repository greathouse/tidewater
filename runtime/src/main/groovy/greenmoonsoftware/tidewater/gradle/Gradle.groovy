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
        ProcessBuilder builder = new ProcessBuilder("${executable} -b ${buildFile} ${tasks}".split(' ') )
        builder.directory(workingDir)
        builder.redirectErrorStream(true)
        Process process = builder.start()
        BufferedReader reader = new BufferedReader (new InputStreamReader(process.inputStream))
        String line
        while ((line = reader.readLine ()) != null) {
            log.println(line)
        }
    }

    File getWorkingDir() {
        workingDir
    }

    Gradle setWorkingDir(String s) {
        workingDir = new File(Context.get().workspace, s)
        return this
    }
}

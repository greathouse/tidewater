package greenmoonsoftware.tidewater.gradle

import greenmoonsoftware.tidewater.config.Context
import greenmoonsoftware.tidewater.config.step.AbstractStep
import groovy.transform.ToString

@ToString
class Gradle extends AbstractStep {
    private File workingDir = Context.get().workspace
    String executable = './gradlew'
    String buildFile = 'build.gradle'
    String tasks = 'clean'

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

    @Override
    Map<String, Object> getInputs() {
        [
                workingDir: workingDir,
                executable: executable,
                buildFile: buildFile,
                tasks: tasks
        ].asImmutable()
    }

    @Override
    Map<String, Object> getOutputs() {
        [:].asImmutable()
    }

    void setWorkingDir(String s) {
        workingDir = new File(Context.get().workspace, s)
    }
}

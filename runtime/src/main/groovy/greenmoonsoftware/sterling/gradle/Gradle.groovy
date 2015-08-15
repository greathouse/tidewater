package greenmoonsoftware.sterling.gradle

import greenmoonsoftware.sterling.config.Context
import greenmoonsoftware.sterling.config.Step
import groovy.transform.ToString

@ToString
class Gradle implements Step {
    private File workingDir = Context.get().workspace
    String executable = './gradlew'
    String buildFile = 'build.gradle'
    String tasks = 'clean'

    @Override
    void execute() {
        ProcessBuilder builder = new ProcessBuilder("${executable} -b ${buildFile} ${tasks}".split(' ') )
        builder.directory(workingDir)
        builder.redirectErrorStream(true)
        Process process = builder.start()
        BufferedReader reader = new BufferedReader (new InputStreamReader(process.inputStream))
        String line
        while ((line = reader.readLine ()) != null) {
            System.out.println (line)
        }
    }

    void setWorkingDir(String s) {
        workingDir = new File(Context.get().workspace, s)
    }
}

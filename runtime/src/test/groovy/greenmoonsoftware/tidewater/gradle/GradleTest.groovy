package greenmoonsoftware.tidewater.gradle

import greenmoonsoftware.tidewater.config.Context
import org.testng.annotations.Test

class GradleTest {

    @Test
    void shouldReturnAllInputs() {
        def context = new Context()
        def executable = 'gradle'
        def buildFile = 'build.gradle'
        def tasks = 'clean build deploy'
        def workingDir = UUID.randomUUID().toString()
        def actual = new Gradle(executable: executable, buildFile: buildFile, tasks: tasks)
                .setWorkingDir(workingDir)
                .inputs

        assert actual.size() == 4

        assert actual['workingDir'] == new File(context.workspace, workingDir)
        assert actual['executable'] == executable
        assert actual['buildFile'] == buildFile
        assert actual['tasks'] == tasks
    }
}

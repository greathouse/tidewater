package greenmoonsoftware.tidewater.plugins.gradle

import greenmoonsoftware.tidewater.config.NewContext
import org.testng.annotations.Test

class GradleTest {

    @Test
    void shouldReturnAllInputs() {
        def context = new NewContext()
        def executable = 'gradle'
        def buildFile = 'build.gradle'
        def tasks = 'clean build deploy'
        def workingDir = UUID.randomUUID().toString()
        def task = new Gradle(executable: executable, buildFile: buildFile, tasks: tasks)
        task.setWorkingDir(workingDir)
        def actual = task.inputs

        assert actual.size() == 4

        assert actual['workingDir'] == workingDir
        assert actual['executable'] == executable
        assert actual['buildFile'] == buildFile
        assert actual['tasks'] == tasks
    }
}

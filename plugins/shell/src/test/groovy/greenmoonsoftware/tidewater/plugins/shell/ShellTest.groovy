package greenmoonsoftware.tidewater.plugins.shell

import greenmoonsoftware.tidewater.run.RunContext
import org.testng.annotations.Test

class ShellTest {
    @Test
    void shouldIncludeAllInputsAndOutputs() {
        def context = new RunContext()
        def contents = 'echo Test'
        def workingDir = UUID.randomUUID().toString()
        def env = [name: 'value']
        def actual = new Shell(contents: contents, workingDir: workingDir, env: env).inputs

        assert actual['contents'] == contents
        assert actual['env'] as Map == env
    }
}

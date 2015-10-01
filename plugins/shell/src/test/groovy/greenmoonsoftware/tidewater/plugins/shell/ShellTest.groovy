package greenmoonsoftware.tidewater.plugins.shell

import greenmoonsoftware.tidewater.config.NewContext
import org.testng.annotations.Test

class ShellTest {
    @Test
    void shouldIncludeAllInputs() {
        def context = new NewContext()
        def contents = 'echo Test'
        def workingDir = UUID.randomUUID().toString()
        def env = [name: 'value']
        def actual = new Shell(contents: contents, workingDir: workingDir, env: env).inputs

        assert actual['contents'] == contents
//        assert actual['workingDir'] == new File(context.workspace, workingDir)
        assert actual['env'] as Map == env
    }
}

package greenmoonsoftware.tidewater.shell

import greenmoonsoftware.tidewater.config.Context
import org.testng.annotations.Test

class ShellTest {
    @Test
    void shouldIncludeAllInputs() {
        def context = new Context()
        def contents = 'echo Test'
        def workingDir = UUID.randomUUID().toString()
        def actual = new Shell(contents: contents, workingDir: workingDir).inputs
        assert actual.size() == 2
        assert actual['contents'] == contents

        assert actual['workingDir'] == new File(context.workspace, workingDir)
    }
}

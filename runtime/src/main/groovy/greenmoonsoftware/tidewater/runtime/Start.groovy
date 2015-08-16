package greenmoonsoftware.tidewater.runtime

import greenmoonsoftware.tidewater.config.Context
import org.codehaus.groovy.control.CompilerConfiguration

class Start {
    static void main(String[] args) {
        new Start().start('''
            import greenmoonsoftware.tidewater.scm.git.*
            import greenmoonsoftware.tidewater.gradle.*
            import greenmoonsoftware.tidewater.shell.*

            step checkout(type: GitClone) {
                url 'https://github.com/greathouse/green-tea-test.git'
                dir 'green-tea-test'
            }

            step buildProject(type: Gradle) {
                workingDir checkout.dir
                tasks 'clean build'
            }

            step custom {
                println 'Workspace: ' + context.workspace
                println 'Checkout dir: ' + checkout.dir
                println 'Sha from checkout: ' + checkout.sha
            }

            step shell(type: Shell) {
                contents """
                    #!/bin/bash
                    echo hello from the shell
                    whoami
                """
            }
        ''')
    }

    public void start(String script) {
        Binding binding = new Binding()
        CompilerConfiguration cc = new CompilerConfiguration();
        cc.setScriptBaseClass(DelegatingScript.class.getName());
        GroovyShell shell = new GroovyShell(this.class.classLoader, binding, cc)
        DelegatingScript script1 = (DelegatingScript)shell.parse(script)

        script1.setDelegate(Context.get())
        script1.run()

        Context.get().execute()
    }
}

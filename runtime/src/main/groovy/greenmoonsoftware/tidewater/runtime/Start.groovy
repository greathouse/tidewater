package greenmoonsoftware.tidewater.runtime

import greenmoonsoftware.tidewater.config.Context
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer

class Start {
    static void main(String[] args) {
        new Start().start('''
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
                contents """\
                    #!/bin/bash
                    echo hello from the shell
                    whoami
                """.stripIndent()
            }
        ''')
    }

    public void start(String script) {
        DelegatingScript script1 = setupScript(script)
        script1.run()

        Context.get().execute()
    }

    private DelegatingScript setupScript(String script) {
        DelegatingScript script1 = parseScript(setupCompilerConfiguration(), script)
        script1.setDelegate(Context.get())
        return script1
    }

    private CompilerConfiguration setupCompilerConfiguration() {
        CompilerConfiguration cc = new CompilerConfiguration();
        cc.setScriptBaseClass(DelegatingScript.class.getName());
        addImports(cc)
        return cc
    }

    private DelegatingScript parseScript(CompilerConfiguration cc, String script) {
        GroovyShell shell = new GroovyShell(this.class.classLoader, new Binding(), cc)
        DelegatingScript script1 = (DelegatingScript) shell.parse(script)
        return script1
    }

    private void addImports(CompilerConfiguration cc) {
        def imports = new ImportCustomizer()
        imports.addStarImports(
                'greenmoonsoftware.tidewater.scm.git',
                'greenmoonsoftware.tidewater.gradle',
                'greenmoonsoftware.tidewater.shell'
        )
        cc.addCompilationCustomizers imports
    }
}
package greenmoonsoftware.sterling.runtime

import greenmoonsoftware.sterling.config.Context
import org.codehaus.groovy.control.CompilerConfiguration

class Start {
    static void main(String[] args) {
        new Start().start("""
            import greenmoonsoftware.sterling.scm.git.*
            import greenmoonsoftware.sterling.gradle.*

            step checkout(type: GitClone) {
                url = 'https://github.com/greathouse/green-tea-test.git'
                dir = 'green-tea-test'
            }

            step buildProject(type: Gradle) {
                workingDir = 'green-tea-test'
                tasks = 'clean build'
            }

        """)
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

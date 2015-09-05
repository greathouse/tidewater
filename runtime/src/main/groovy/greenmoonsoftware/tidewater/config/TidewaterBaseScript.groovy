package greenmoonsoftware.tidewater.config

import greenmoonsoftware.tidewater.step.CustomStep
import greenmoonsoftware.tidewater.step.StepConfiguration
import greenmoonsoftware.tidewater.step.events.StepConfiguredEvent
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer

abstract class TidewaterBaseScript extends Script implements Serializable {
    def step(StepConfiguration definition) {
        this.binding.context.raiseEvent(new StepConfiguredEvent(definition))
    }

    def methodMissing(String name, args) {
        if (args.length == 1) {
            return customStep(name, args)
        }
        return stepConfiguration(name, args)
    }

    StepConfiguration stepConfiguration(String name, args) {
        def type = args[0].type
        def configureClosure = args[-1]
        return new StepConfiguration(name:name, type: type, configureClosure: configureClosure)
    }

    StepConfiguration customStep(String name, args) {
        Closure c = args[0]
        return new StepConfiguration(name: name, type: CustomStep, configureClosure: { executable c})
    }

    final static TidewaterBaseScript configure(Context c, String script) {
        return setupScript(c, script)
    }

    private static TidewaterBaseScript setupScript(Context c, String script) {
        def script1 = parseScript(c, setupCompilerConfiguration(), script)
        return script1
    }

    private static CompilerConfiguration setupCompilerConfiguration() {
        CompilerConfiguration cc = new CompilerConfiguration();
        cc.setScriptBaseClass(TidewaterBaseScript.class.getName());
        addImports(cc)
        return cc
    }

    private static TidewaterBaseScript parseScript(Context c, CompilerConfiguration cc, String script) {
        GroovyShell shell = new GroovyShell(this.class.classLoader, new Binding(context: c), cc)
        def script1 = (TidewaterBaseScript) shell.parse(script)
        return script1
    }

    private static void addImports(CompilerConfiguration cc) {
        def imports = new ImportCustomizer()
        imports.addStarImports(
                'greenmoonsoftware.tidewater.scm.git',
                'greenmoonsoftware.tidewater.gradle',
                'greenmoonsoftware.tidewater.shell'
        )
        cc.addCompilationCustomizers imports
    }
}
package greenmoonsoftware.tidewater.context
import greenmoonsoftware.tidewater.Context
import greenmoonsoftware.tidewater.step.StepDefinition
import org.codehaus.groovy.control.CompilerConfiguration

abstract class TidewaterBaseScript extends Script implements Serializable {
    def parameters(List parameterNames) {
        new ParameterResolver(parameterNames).resolve().each {
            this.binding.context.setParameter(it.key, it.value)
        }
    }

    def properties(Closure c) {
        this.binding.context.with c
    }

    def step(StepDefinition definition) {
        this.binding.context.addDefinedStep(definition)
    }

    def methodMissing(String name, args) {
        return StepDefinition.builder()
                .name(name)
                .scriptArgs(args)
                .build()
    }

    void println(String s) {
        this.binding.context.log(s)
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
        return cc
    }

    private static TidewaterBaseScript parseScript(Context c, CompilerConfiguration cc, String script) {
        GroovyShell shell = new GroovyShell(Thread.currentThread().contextClassLoader, new Binding(context: c), cc)
        def script1 = (TidewaterBaseScript) shell.parse(script)
        return script1
    }
}

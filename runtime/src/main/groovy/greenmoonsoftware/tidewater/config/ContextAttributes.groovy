package greenmoonsoftware.tidewater.config

import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.StepDefinition

final class ContextAttributes implements Serializable {
    private final ContextId id
    private String script
    private final definedSteps = [:] as LinkedHashMap<String, StepDefinition>
    private final executedSteps = [:] as LinkedHashMap<String, Step>
    private final File workspace
    private final File metaDirectory

    ContextAttributes(ContextId id) {
        this.id = id
        workspace = new File("${Tidewater.WORKSPACE_ROOT}/$id")
        metaDirectory = new File(workspace, '.meta')
    }

    ContextId getId() {
        id
    }

    File getWorkspace() {
        workspace
    }

    File getMetaDirectory() {
        metaDirectory
    }

    Map<String, StepDefinition> getDefinedSteps() {
        definedSteps.asImmutable()
    }

    void addDefinedStep(StepDefinition stepDef) {
        definedSteps[stepDef.name] = stepDef
    }

    Map<String, Step> getExecutedSteps() {
        executedSteps.asImmutable()
    }

    void addExecutedStep(Step step) {
        executedSteps[step.name] = step
    }

    String getScript() {
        script
    }

    void setScript(String script) {
        if (this.script) {
            throw new RuntimeException('Script is already set. Cannot change.')
        }
        this.script = script
    }
}

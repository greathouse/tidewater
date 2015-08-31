package greenmoonsoftware.tidewater.config

import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.StepConfiguration

final class ContextAttributes implements Serializable {
    private final String id
    private final definedSteps = [:] as LinkedHashMap<String, StepConfiguration>
    private final executedSteps = [:] as LinkedHashMap<String, Step>
    private final File workspace
    private final File metaDirectory

    ContextAttributes(String id) {
        this.id = id
        workspace = new File("${Tidewater.WORKSPACE_ROOT}/$id")
        metaDirectory = new File(workspace, '.meta')
    }

    String getId() {
        id
    }

    File getWorkspace() {
        workspace
    }

    File getMetaDirectory() {
        metaDirectory
    }

    Map getDefinedSteps() {
        definedSteps.asImmutable()
    }

    void addDefinedStep(StepConfiguration stepDef) {
        definedSteps[stepDef.name] = stepDef
    }

    Map getExecutedSteps() {
        executedSteps.asImmutable()
    }

    void addExecutedStep(Step step) {
        executedSteps[step.name] = step
    }
}

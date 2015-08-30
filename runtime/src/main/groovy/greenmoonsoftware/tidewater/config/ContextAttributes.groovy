package greenmoonsoftware.tidewater.config

import greenmoonsoftware.tidewater.config.step.Step
import greenmoonsoftware.tidewater.config.step.StepConfiguration

final class ContextAttributes implements Serializable {
    private final definedSteps = [:] as LinkedHashMap<String, StepConfiguration>
    private final executedSteps = [:] as LinkedHashMap<String, Step>
    private final workspace = new File("${Tidewater.WORKSPACE_ROOT}/${new Date().format('yyyyMMddHHmmssSSSS')}")
    private final metaDirectory = new File(workspace, '.meta')

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

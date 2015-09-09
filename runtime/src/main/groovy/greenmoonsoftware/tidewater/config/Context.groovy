package greenmoonsoftware.tidewater.config
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.StepDefinition

interface Context {
    File getWorkspace()
    File getMetaDirectory()
    void addDefinedStep(StepDefinition definition)
    Step findExecutedStep(String name)
    void raiseEvent(Event event)
    void log(Step s, String message)
    void log(String message)
}
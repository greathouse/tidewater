package greenmoonsoftware.tidewater.context
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.tidewater.step.Step
import greenmoonsoftware.tidewater.step.StepDefinition

interface Context {
    String getParameter(String name)
    void setParameter(String name, String value)
    Map<String, String> getParameters()
    ContextAttributes getAttributes()
    def getExt(String name)
    void setExt(String name, Object value)
    @Deprecated File getWorkspace()
    @Deprecated File getMetaDirectory()
    void addDefinedStep(StepDefinition definition)
    void addExecutedStep(Step step)
    Step findExecutedStep(String name)
    void raiseEvent(Event event)
    void log(Step s, String message)
    void log(String message)
}
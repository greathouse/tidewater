package greenmoonsoftware.tidewater.config

import greenmoonsoftware.es.event.Event
import greenmoonsoftware.tidewater.step.Step

interface Context {
    File getWorkspace()
    File getMetaDirectory()
    Step findExecutedStep(String name)
    void raiseEvent(Event event)
    void log(Step s, String message)
    void log(String message)
}
package greenmoonsoftware.tidewater.runtime

import greenmoonsoftware.tidewater.config.ContextId
import greenmoonsoftware.tidewater.restart.RestartContext

class Retry {
    static void main(String[] args) {
        def restart = new RestartContext(new ContextId('2015-09-05_22-35-43'))
        restart.addEventSubscribers(new StdoutLoggingSubscriber())
        restart.restart()
    }
}

package greenmoonsoftware.tidewater.runtime

import greenmoonsoftware.tidewater.config.replay.ReplayContext

class Replay {
    static void main(String[] args) {
        def context = new ReplayContext('2015-08-30_21-00-25')
        context.addEventSubscribers(new StdoutLoggingSubscriber())
        context.replay()
    }
}

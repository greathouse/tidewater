package greenmoonsoftware.tidewater.runtime

import greenmoonsoftware.tidewater.replay.ReplayRunner

class Replay {
    static void main(String[] args) {
        def context = new ReplayRunner('2015-08-30_21-00-25')
        context.addEventSubscribers(new StdoutLoggingSubscriber())
        context.replay()
    }
}

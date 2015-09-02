package greenmoonsoftware.tidewater.step
import greenmoonsoftware.es.event.jdbcstore.ObjectEventSerializer
import greenmoonsoftware.tidewater.DummyStep
import greenmoonsoftware.tidewater.step.events.StepSuccessfullyCompletedEvent
import org.testng.annotations.Test

import java.time.Duration
import java.time.Instant

class StepSuccessfullyCompletedEventTest {

    @Test
    void shouldSerializeAndDeserialize() {
        def startTime = Instant.now().minusSeconds(5)
        def endTime = Instant.now()
        def endDate = new Date()
        def duration = Duration.between(startTime, endTime)
        def step = new DummyStep()
        def event = new StepSuccessfullyCompletedEvent(step, endDate, duration)

        def serializer = new ObjectEventSerializer()

        def actual = serializer.deserialize(serializer.serialize(event)) as StepSuccessfullyCompletedEvent

        assert actual
        assert actual.aggregateId == event.aggregateId
        assert actual.eventDateTime == event.eventDateTime
        assert actual.step == step
        assert actual.endDate == event.endDate
        assert actual.duration == event.duration
    }
}

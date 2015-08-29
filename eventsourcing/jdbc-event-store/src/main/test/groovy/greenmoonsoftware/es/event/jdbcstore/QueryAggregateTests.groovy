package greenmoonsoftware.es.event.jdbcstore

import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.SimpleEvent
import groovy.transform.EqualsAndHashCode
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import java.time.Instant

class QueryAggregateTests extends JdbcStoreTestBase {
    @BeforeMethod
    void setupNegativeEvents() {
        postEvents(UUID.randomUUID())
    }

    @Test
    void givenEventsHaveHappened_shouldApplyEventsInOrder() {
        def aggregateId = UUID.randomUUID()
        List<Event> expectedEvents = postEvents(aggregateId, 'string1', 'string2')
        def query = new TestJdbcStoreQuery(new JdbcStoreConfiguration(tablename), datasource)
        def aggregate = query.retrieve(aggregateId.toString())
        assert aggregate
        assert aggregate.events.size() == expectedEvents.size()
        expectedEvents.eachWithIndex { expected, idx ->
            assert aggregate.events[idx] == expected
        }
    }

    @Test
    void givenEventsPostedOutOfOrder_shouldApplyEventInOrder() {
        Instant now = Instant.now()
        def aggregateId = UUID.randomUUID().toString()
        def event1 = new SimpleEvent.Builder('1', aggregateId).eventDateTime(now.minusSeconds(10)).build()
        def event2 = new SimpleEvent.Builder('2', aggregateId).eventDateTime(now.minusSeconds(5)).build()
        def event3 = new SimpleEvent.Builder('3', aggregateId).eventDateTime(now).build()
        def eventsOutOfOrder = [event3, event1, event2]
        eventsOutOfOrder.each { eventBus.post(it) }

        def query = new TestJdbcStoreQuery(new JdbcStoreConfiguration(tablename), datasource)
        def aggregate = query.retrieve(aggregateId)

        assert aggregate instanceof TestJdbcStoreQuery.TestAggregate
        assert aggregate.events.size() == eventsOutOfOrder.size()
        assert aggregate.events[0] == event1
        assert aggregate.events[1] == event2
        assert aggregate.events[2] == event3
    }

    @Test
    void givenMixedEvents_shouldApplyEvents() {
        def aggregateId = UUID.randomUUID()
        def event1 = new SimpleEvent.Builder('1', aggregateId.toString()).build()
        def event2 = new ExplicitEvent(aggregateId: aggregateId)
        def events = [event1, event2]
        events.each { eventBus.post(it) }

        def query = new TestJdbcStoreQuery(new JdbcStoreConfiguration(tablename), datasource)
        def aggregate = query.retrieve(aggregateId.toString())

        assert aggregate
        assert aggregate.events.size() == events.size()
        assert aggregate.events[0] == event1
        assert aggregate.events[1] == event2
    }

    @Test
    void givenNullOrEmptyAggregateId_shouldThrowException() {
        def data = [null, '']
        def query = new TestJdbcStoreQuery(new JdbcStoreConfiguration(tablename), datasource)
        data.each {
            try {
                query.retrieve(null)
                Assert.fail('Should have raised exception')
            }
            catch(IllegalArgumentException e) {
                assert true
            }
        }
    }

    private List<Event> postEvents(UUID aggregateId, String...parameterNames) {
        List<Event> expectedEvents = []
        (1..4).each {
            def builder = new SimpleEvent.Builder(it.toString(), aggregateId.toString())
            parameterNames.each {
                builder.parameter(it, UUID.randomUUID().toString())
            }
            def event = builder.build()
            expectedEvents << event
            eventBus.post(event)
        }
        expectedEvents
    }

    @EqualsAndHashCode
    public static class ExplicitEvent implements Event {
        UUID id = UUID.randomUUID()
        String type = 'my-type'
        String aggregateId = UUID.randomUUID().toString()
        Instant eventDateTime = Instant.now()
    }
}

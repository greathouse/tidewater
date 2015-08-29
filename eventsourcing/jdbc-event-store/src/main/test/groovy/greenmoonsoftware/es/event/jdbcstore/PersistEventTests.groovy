package greenmoonsoftware.es.event.jdbcstore
import greenmoonsoftware.es.event.SimpleEvent
import org.testng.annotations.Test

import java.sql.Timestamp
import java.time.Instant

class PersistEventTests extends JdbcStoreTestBase {
    @Test
    void givenEvent_shouldPersist() {
        def event = new SimpleEvent.Builder('jdbc-event', UUID.randomUUID().toString()).build()
        eventBus.post(event)

        assert sql.firstRow('select count(*) cnt from ' + tablename).cnt == 1
        def row = sql.firstRow('select * from ' + tablename)
        assert row.id
        assert row.aggregateId
        assert row.eventType == event.type
        assert row.eventDateTime
        assert row.savedTimestamp
        assert row.data
    }

    @Test
    void givenEventWithATimestamp_shouldPersistGivenTimestamp() {
        def expectedEventDateTime = Instant.ofEpochSecond(3181000000)
        def event = new SimpleEvent.Builder('jdbc-event', UUID.randomUUID().toString()).eventDateTime(expectedEventDateTime).build()
        eventBus.post(event)

        assert sql.firstRow('select count(*) cnt from ' + tablename).cnt == 1
        def row = sql.firstRow('select * from ' + tablename)
        println row.eventDateTime
        assert row.eventDateTime == new Timestamp(expectedEventDateTime.toEpochMilli())
    }
}

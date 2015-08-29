package greenmoonsoftware.es.event.jdbcstore
import greenmoonsoftware.es.event.Aggregate
import greenmoonsoftware.es.event.EventList

import javax.sql.DataSource

class TestJdbcStoreQuery extends JdbcStoreQuery<TestAggregate> {
    TestJdbcStoreQuery(
            JdbcStoreConfiguration config,
            DataSource ds
    ) {
        super(config, ds)
    }

    @Override
    protected TestAggregate create() {
        new TestAggregate()
    }

    public class TestAggregate implements Aggregate {
        String id = UUID.randomUUID().toString()
        EventList events

        @Override
        void apply(EventList eventList) {
            events = eventList
        }
    }
}

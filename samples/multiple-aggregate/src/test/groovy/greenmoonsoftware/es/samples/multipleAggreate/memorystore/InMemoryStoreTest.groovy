package greenmoonsoftware.es.samples.multipleAggreate.memorystore

import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.es.samples.multipleAggreate.AggregateTestBase
import greenmoonsoftware.es.samples.user.User
import greenmoonsoftware.es.store.InMemoryEventStore
import greenmoonsoftware.es.store.StoreRetrieval
import org.testng.annotations.Test

class InMemoryStoreTest extends AggregateTestBase {
    InMemoryEventStore<User> store = new InMemoryEventStore<User>() {
        @Override
        protected User create() {
            new User();
        }
    }

    @Override
    StoreRetrieval<User> getQuery() {
        store
    }

    @Override
    EventSubscriber<Event> getStore() {
        store
    }

    @Test
    void test() {
        assert true
    }
}

package greenmoonsoftware.es.store;

import greenmoonsoftware.es.event.Aggregate;

public interface StoreRetrieval<T extends Aggregate> {
    T retrieve(String aggregateId);
}

package greenmoonsoftware.es.event;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public interface Event extends Serializable {
    UUID getId();

    String getAggregateId();

    String getType();

    Instant getEventDateTime();
}

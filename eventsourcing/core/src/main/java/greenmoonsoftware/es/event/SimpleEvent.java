package greenmoonsoftware.es.event;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class SimpleEvent implements Event {
    private final UUID id;
    private String aggregateId;
    private final String type;
    private Instant eventDateTime;
    private final Map<String, Object> parameters;

    private SimpleEvent(SimpleEvent.Builder builder) {
        id = UUID.randomUUID();
        type = builder.type;
        aggregateId = builder.aggregateId;
        eventDateTime = builder.eventDateTime;
        parameters = Collections.unmodifiableMap(builder.parameters);
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getAggregateId() {
        return aggregateId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleEvent that = (SimpleEvent) o;

        if (!eventDateTime.equals(that.eventDateTime)) return false;
        if (!id.equals(that.id)) return false;
        if (!parameters.equals(that.parameters)) return false;
        if (!type.equals(that.type)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        final int i = 3181;
        result = i * result + type.hashCode();
        result = i * result + eventDateTime.hashCode();
        result = i * result + parameters.hashCode();
        return result;
    }

    @Override
    public Instant getEventDateTime() {
        return eventDateTime;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public static class Builder {
        private final String type;
        private final String aggregateId;
        private Instant eventDateTime = Instant.now();
        private final Map<String, Object> parameters = new HashMap<>();

        public Builder(String type, String aggregateId) {
            this.type = type;
            this.aggregateId = aggregateId;
        }

        public SimpleEvent build() {
            return new SimpleEvent(this);
        }

        public Builder eventDateTime(Instant dateTime) {
            this.eventDateTime = dateTime;
            return this;
        }

        public Builder parameter(String key, Object value) {
            parameters.put(key, value);
            return this;
        }

        public Builder parameters(Map<String, Object> params) {
            parameters.putAll(params);
            return this;
        }
    }
}

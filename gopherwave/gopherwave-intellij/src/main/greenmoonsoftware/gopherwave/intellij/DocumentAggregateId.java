package greenmoonsoftware.gopherwave.intellij;

import java.util.Objects;

public final class DocumentAggregateId {
    public static final DocumentAggregateId INVALID = new DocumentAggregateId("");

    private String value;

    public DocumentAggregateId(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentAggregateId that = (DocumentAggregateId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

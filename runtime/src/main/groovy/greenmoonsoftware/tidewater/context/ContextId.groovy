package greenmoonsoftware.tidewater.context
import groovy.transform.Canonical

@Canonical
class ContextId implements Serializable {
    private final String value

    protected ContextId(){}

    ContextId(String id) {
        this.value = id
    }

    String getId() {
        value
    }

    @Override
    String toString() {
        value
    }
}

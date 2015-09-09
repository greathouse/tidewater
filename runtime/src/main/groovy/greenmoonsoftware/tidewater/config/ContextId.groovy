package greenmoonsoftware.tidewater.config

class ContextId implements Serializable {
    private final String value

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

package greenmoonsoftware.tidewater.config

class ContextId implements Serializable {
    private final String value

    ContextId(String id) {
        this.value = id
    }

    @Override
    String toString() {
        value
    }
}

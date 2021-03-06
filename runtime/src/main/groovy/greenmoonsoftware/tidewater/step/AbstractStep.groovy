package greenmoonsoftware.tidewater.step

abstract class AbstractStep implements Step {
    String name

    @Override
    Map<String, Object> getInputs() {
        findFieldsWith(Input)
    }

    @Override
    Map<String, Object> getOutputs() {
        findFieldsWith(Output)
    }

    private Map<String, Object> findFieldsWith(Class annotation) {
        this.class.declaredFields
                .findAll { field -> field.isAnnotationPresent(annotation) }
                .collectEntries { field -> [(field.name): this[field.name]] }
    }
}

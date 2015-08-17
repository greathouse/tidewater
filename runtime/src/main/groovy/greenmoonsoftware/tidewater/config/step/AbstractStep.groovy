package greenmoonsoftware.tidewater.config.step

abstract class AbstractStep implements Step {
    @Override
    final void seralize(File toFile) {

    }

    @Override
    final void unserialize(File fromFile) {

    }

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

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
        this.class.declaredFields
                .findAll { field -> field.isAnnotationPresent(Input)}
                .collectEntries { field -> [(field.name) : this[field.name]]}
    }

    @Override
    Map<String, Object> getOutputs() {
        this.class.declaredFields
                .findAll { field -> field.isAnnotationPresent(Output)}
                .collectEntries { field -> [(field.name) : this[field.name]]}
    }
}

package greenmoonsoftware.tidewater.config.step
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

abstract class AbstractStep implements Step {
    String name

    @Override
    final void seralize(File toFile) {
        def json = new JsonBuilder()
        json.step {
            'inputs' inputs
            'outputs' outputs
        }
        toFile.write(json.toString())
    }

    @Override
    final void deserialize(File fromFile) {
        def json = new JsonSlurper().parse(fromFile)
        json.step.inputs.each { i ->
            this[i.key] = i.value
        }
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

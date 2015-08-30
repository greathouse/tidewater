package greenmoonsoftware.tidewater.config.step

class StepConfiguration implements Serializable {
    String name
    Class type
    Closure configureClosure
}

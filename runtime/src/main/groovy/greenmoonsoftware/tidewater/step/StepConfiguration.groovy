package greenmoonsoftware.tidewater.step

class StepConfiguration implements Serializable {
    String name
    Class type
    transient Closure configureClosure
}

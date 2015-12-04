package greenmoonsoftware.tidewater.step

import org.testng.annotations.Test

class StepDefinitionBuilderTest {
    @Test
    void givenName_shouldReturnDefinitionWithGivenName() {
        def expected = 'Expected'
        def actual = StepDefinition.builder().name(expected).build()
        assert actual.name == expected
    }

    @Test
    void givenSingleArgumentAsClosure_shouldReturnDefinitionOfTypeCustomStep() {
        def expectedClosure = configureClosure()
        def args = [expectedClosure]
        def actual = StepDefinition.builder().scriptArgs(args).build()
        assert actual.type == CustomStep.canonicalName
        assert actual.configureClosure == expectedClosure
    }

    @Test
    void givenDefinitionArguementsWithType_shouldReturnDefinitionOfType() {
        def expected = 'greenmoonsoftware.tidewater.plugins.test.TestStep'
        def expectedClosure = configureClosure()
        def args = [[type: expected], expectedClosure]
        def actual = StepDefinition.builder().scriptArgs(args).build()
        assert actual.type == expected
        assert actual.configureClosure == expectedClosure
    }

    @Test
    void givenTypeWithEnabledSet_shouldSetProperty() {
        def type = 'greenmoonsoftware.tidewater.plugins.test.TestStep'
        [true, false].each { expected ->
            def args = [[type: type, enabled: expected], configureClosure()]
            def actual = StepDefinition.builder().scriptArgs(args).build()
            assert actual.enabled == expected, "Failed for enabled == ${expected}"
        }
    }

    @Test
    void givenNoTypeWithEnabledSet_shouldReturnCustomStepAndEnabledProperty() {
        [true, false].each { expected ->
            def args = [[enabled: expected], configureClosure()]
            def actual = StepDefinition.builder().scriptArgs(args).build()
            assert actual.enabled == expected, "Failed for enabled == ${expected}"
            assert actual.type == CustomStep.canonicalName
        }
    }

    private configureClosure() {
        return { def someClosure = true }
    }
}

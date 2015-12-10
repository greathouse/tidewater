package greenmoonsoftware.tidewater.step

import org.testng.annotations.DataProvider
import org.testng.annotations.Test

class StepResultTest {

    @DataProvider
    Object[][] values() {[
            [true, StepResult.SUCCESS],
            [false, StepResult.FAILURE],
            ['string', StepResult.SUCCESS],
            [null, StepResult.FAILURE]

    ]}

    @Test(dataProvider = 'values')
    void testFrom(Object test, StepResult expected) {
        assert StepResult.from(test) == expected
    }
}

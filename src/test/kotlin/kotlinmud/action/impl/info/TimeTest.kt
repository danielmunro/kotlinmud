package kotlinmud.action.impl.info

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.helper.createTestService
import org.junit.Test

class TimeTest {
    @Test
    fun testTimeDisplay() {
        // setup
        val test = createTestService()

        // given
        test.incrementTicks(30)

        // when
        val response = test.runAction("time")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("it is the 1st day of month 1 of year 1")
    }
}

package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.helper.createTestService
import org.junit.Test

class AreaTest {
    @Test
    fun testCanSeeArea() {
        // setup
        val testService = createTestService()

        // when
        val response = testService.runAction("area")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("area: Test")
    }
}

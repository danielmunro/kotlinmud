package kotlinmud.action.impl.social

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.createTestService
import org.junit.Test

class TellTest {
    @Test
    fun testCanTellMob() {
        // setup
        val testService = createTestService()
        testService.createMobBuilder().name("foo").build()
        testService.createMobBuilder().name("bar").build()

        // when
        val response = testService.runAction("tell bar hello world")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you tell bar, \"hello world\"")
    }
}

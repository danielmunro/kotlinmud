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
        val mob1 = testService.createMob { it.name = "foo" }
        testService.createMob { it.name = "bar" }

        // when
        val response = testService.runAction(mob1, "tell bar hello world")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you tell bar, \"hello world\"")
    }
}

package kotlinmud.action.impl.social

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.helper.createTestService
import org.junit.Test

class TellTest {
    @Test
    fun testCanTellMob() {
        // setup
        val testService = createTestService()
        testService.createPlayerMob("foo")
        testService.createPlayerMob("bar")

        // when
        val response = testService.runAction("tell bar hello world")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you tell bar, \"hello world\"")
    }
}

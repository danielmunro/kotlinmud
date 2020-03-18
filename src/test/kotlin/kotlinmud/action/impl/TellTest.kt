package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.createTestService
import kotlinmud.test.getIdentifyingWord
import org.junit.Test

class TellTest {
    @Test
    fun testCanTellMob() {
        // setup
        val testService = createTestService()
        val mob1 = testService.createMob()
        val mob2 = testService.createMob()

        // when
        val response = testService.runAction(mob1, "tell ${getIdentifyingWord(mob2)} hello world")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you tell $mob2, \"hello world\"")
    }
}

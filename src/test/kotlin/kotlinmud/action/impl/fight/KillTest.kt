package kotlinmud.action.impl.fight

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.helper.createTestService
import kotlinmud.test.helper.getIdentifyingWord
import org.junit.Test

class KillTest {
    @Test
    fun testCannotKillQuestMobs() {
        // setup
        val test = createTestService()
        test.createMob()

        // given
        val target = test.createQuestGiver()

        // when
        val response = test.runAction("kill ${getIdentifyingWord(target)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you cannot target them.")
    }

    @Test
    fun testCannotKillShopkeepers() {
        // setup
        val test = createTestService()
        test.createMob()

        // given
        val target = test.createShopkeeper()

        // when
        val response = test.runAction("kill ${getIdentifyingWord(target)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you cannot target them.")
    }
}

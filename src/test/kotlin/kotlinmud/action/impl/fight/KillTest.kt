package kotlinmud.action.impl.fight

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.mob.type.JobType
import kotlinmud.test.createTestService
import kotlinmud.test.getIdentifyingWord
import org.junit.Test

class KillTest {
    @Test
    fun testCannotKillQuestMobs() {
        // setup
        val test = createTestService()
        test.createMob()

        // given
        val target = test.createMob {
            it.job = JobType.QUEST
        }

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
        val target = test.createMob {
            it.job = JobType.SHOPKEEPER
        }

        // when
        val response = test.runAction("kill ${getIdentifyingWord(target)}")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you cannot target them.")
    }
}

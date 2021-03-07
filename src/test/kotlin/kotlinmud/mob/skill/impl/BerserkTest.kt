package kotlinmud.mob.skill.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.io.type.IOStatus
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.test.helper.createTestService
import org.junit.Test

class BerserkTest {
    @Test
    fun testMobCanBerserk() {
        // setup
        val testService = createTestService()

        // given
        val mob = testService.createPlayerMob {
            it.skills[SkillType.BERSERK] = 100
        }

        // when
        val response = testService.runActionForIOStatus(mob, "berserk", IOStatus.OK)

        // then
        assertThat(response.message.toActionCreator).isEqualTo("Your pulse speeds up as you are consumed by rage!")
    }
}

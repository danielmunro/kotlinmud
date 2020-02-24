package kotlinmud.mob.skill.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.io.IOStatus
import kotlinmud.mob.skill.SkillType
import kotlinmud.test.createTestService
import org.junit.Test

class BerserkTest {
    @Test
    fun testMobCanBerserk() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        mob.skills = mob.skills.plus(Pair(SkillType.BERSERK, 100))

        // when
        val response = testService.runActionForIOStatus(mob, "berserk", IOStatus.OK)

        // then
        assertThat(response.message.toActionCreator).isEqualTo("Your pulse speeds up as you are consumed by rage!")
    }
}
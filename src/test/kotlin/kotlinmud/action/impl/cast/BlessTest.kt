package kotlinmud.action.impl.cast

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.io.type.IOStatus
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.test.helper.createTestService
import org.junit.Test

class BlessTest {
    @Test
    fun testCanCastBless() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createPlayerMob {
            it.skills[SkillType.BLESS] = 100
        }

        // when
        val response = test.runActionForIOStatus(mob, "cast bless", IOStatus.OK)

        // then
        assertThat(response.message.toActionCreator).isEqualTo("A faint glow surrounds you.")
        assertThat(response.message.toTarget).isEqualTo("A faint glow surrounds you.")
        assertThat(response.message.toObservers).isEqualTo("A faint glow surrounds $mob.")
    }
}

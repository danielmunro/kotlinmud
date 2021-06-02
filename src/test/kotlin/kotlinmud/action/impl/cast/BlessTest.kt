package kotlinmud.action.impl.cast

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import kotlinmud.affect.type.AffectType
import kotlinmud.io.type.IOStatus
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.test.helper.createTestService
import kotlinmud.test.helper.getIdentifyingWord
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

        // and
        assertThat(mob.affects.find { it.type == AffectType.BLESS }).isNotNull()
    }

    @Test
    fun testCanCastBlessOnOthers() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createPlayerMob {
            it.skills[SkillType.BLESS] = 100
        }
        val target = test.createMob()

        // when
        val response = test.runActionForIOStatus(mob, "cast bless ${getIdentifyingWord(target)}", IOStatus.OK)

        // then
        assertThat(response.message.toActionCreator).isEqualTo("A faint glow surrounds $target.")
        assertThat(response.message.toTarget).isEqualTo("A faint glow surrounds you.")
        assertThat(response.message.toObservers).isEqualTo("A faint glow surrounds $target.")

        // and
        assertThat(target.affects.find { it.type == AffectType.BLESS }).isNotNull()
    }
}

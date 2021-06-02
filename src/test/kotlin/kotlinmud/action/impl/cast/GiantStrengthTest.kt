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

class GiantStrengthTest {
    @Test
    fun testCanCastGiantStrength() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createPlayerMob {
            it.skills[SkillType.GIANT_STRENGTH] = 100
        }

        // when
        val response = test.runActionForIOStatus(mob, "cast 'giant strength'", IOStatus.OK)

        // then
        assertThat(response.message.toActionCreator).isEqualTo("your muscles surge with heightened power.")
        assertThat(response.message.toTarget).isEqualTo("your muscles surge with heightened power.")
        assertThat(response.message.toObservers).isEqualTo("${mob.name}'s muscles surge with heightened power.")

        // and
        assertThat(mob.affects.find { it.type == AffectType.GIANT_STRENGTH }).isNotNull()
    }

    @Test
    fun testCanCastGiantStrengthOnOthers() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createPlayerMob {
            it.skills[SkillType.BLESS] = 100
        }
        val target = test.createMob()

        // when
        val response = test.runActionForIOStatus(mob, "cast 'giant strength' ${getIdentifyingWord(target)}", IOStatus.OK)

        // then
        assertThat(response.message.toActionCreator).isEqualTo("${target.name}'s muscles surge with heightened power.")
        assertThat(response.message.toTarget).isEqualTo("your muscles surge with heightened power.")
        assertThat(response.message.toObservers).isEqualTo("${target.name}'s muscles surge with heightened power.")

        // and
        assertThat(target.affects.find { it.type == AffectType.GIANT_STRENGTH }).isNotNull()
    }
}

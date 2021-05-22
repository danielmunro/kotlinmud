package kotlinmud.action.impl.cast

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import kotlinmud.io.type.IOStatus
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.test.helper.createTestService
import kotlinmud.test.helper.getIdentifyingWord
import org.junit.Test

class HealTest {
    @Test
    fun testCanCastHealOnSelfImplicit() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createMob {
            it.skills[SkillType.HEAL] = 100
        }
        mob.hp = 1

        // when
        val response = test.runAction("cast heal")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you are surrounded by a warm glow.")
        assertThat(response.message.toTarget).isEqualTo("you are surrounded by a warm glow.")
        assertThat(response.message.toObservers).isEqualTo("$mob is surrounded by a warm glow.")

        // and
        assertThat(mob.hp).isGreaterThan(1)
    }

    @Test
    fun testCanCastHealOnOthers() {
        // setup
        val test = createTestService()

        // given
        val caster = test.createPlayerMob {
            it.skills[SkillType.HEAL] = 100
        }
        val target = test.createMob()

        // when
        val response = test.runActionForIOStatus(
            caster,
            "cast heal ${getIdentifyingWord(target)}",
            IOStatus.OK
        )

        // then
        assertThat(response.message.toActionCreator).isEqualTo("$target is surrounded by a warm glow.")
        assertThat(response.message.toTarget).isEqualTo("you are surrounded by a warm glow.")
        assertThat(response.message.toObservers).isEqualTo("$target is surrounded by a warm glow.")
    }
}
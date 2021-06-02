package kotlinmud.action.impl.cast

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import kotlinmud.io.type.IOStatus
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.test.helper.createTestService
import kotlinmud.test.helper.getIdentifyingWord
import org.junit.Test

class CureSeriousTest {
    @Test
    fun testCanCureSeriousOnSelfImplicit() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createMob {
            it.skills[SkillType.CURE_SERIOUS] = 100
        }
        mob.hp = 1

        // when
        val response = test.runAction("cast 'cure serious'")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you feel better!")
        assertThat(response.message.toTarget).isEqualTo("you feel better!")
        assertThat(response.message.toObservers).isEqualTo("$mob feels better!")

        // and
        assertThat(mob.hp).isGreaterThan(1)
    }

    @Test
    fun testCanCureSeriousOnOthers() {
        // setup
        val test = createTestService()

        // given
        val caster = test.createPlayerMob {
            it.skills[SkillType.CURE_SERIOUS] = 100
        }
        val target = test.createMob()

        // when
        val response = test.runActionForIOStatus(
            caster,
            "cast 'cure serious' ${getIdentifyingWord(target)}",
            IOStatus.OK
        )

        // then
        assertThat(response.message.toActionCreator).isEqualTo("$target feels better!")
        assertThat(response.message.toTarget).isEqualTo("you feel better!")
        assertThat(response.message.toObservers).isEqualTo("$target feels better!")
    }
}

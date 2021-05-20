package kotlinmud.action.impl.cast.healing

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.io.type.IOStatus
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.test.helper.createTestService
import kotlinmud.test.helper.getIdentifyingWord
import org.junit.Test

class CureLightTest {
    @Test
    fun testCanCureLightOnSelfImplicit() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createMob {
            it.skills[SkillType.CURE_LIGHT] = 100
        }

        // when
        val response = test.runAction("cast 'cure light'")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you feel better!")
        assertThat(response.message.toTarget).isEqualTo("you feel better!")
        assertThat(response.message.toObservers).isEqualTo("$mob feels better!")
    }

    @Test
    fun testCanCureLightOnOthers() {
        // setup
        val test = createTestService()

        // given
        val caster = test.createPlayerMob {
            it.skills[SkillType.CURE_LIGHT] = 100
        }
        val target = test.createMob()

        // when
        val response = test.runActionForIOStatus(
            caster,
            "cast 'cure light' ${getIdentifyingWord(target)}",
            IOStatus.OK
        )

        // then
        assertThat(response.message.toActionCreator).isEqualTo("$target feels better!")
        assertThat(response.message.toTarget).isEqualTo("you feel better!")
        assertThat(response.message.toObservers).isEqualTo("$target feels better!")
    }
}

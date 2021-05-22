package kotlinmud.action.impl.fight

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.io.type.IOStatus
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.test.helper.createTestService
import kotlinmud.test.helper.getIdentifyingWord
import org.junit.Test

class BashTest {
    @Test
    fun testCanTargetMob() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createPlayerMob {
            it.skills[SkillType.BASH] = 100
        }
        val target = test.createMob()

        // when
        val response = test.runActionForIOStatus(mob, "bash ${getIdentifyingWord(target)}", IOStatus.OK) {
            mob.mv = 100
        }

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you slam into $target and send them flying!")
        assertThat(response.message.toTarget).isEqualTo("$mob slams into you and sends you flying!")
        assertThat(response.message.toObservers).isEqualTo("$mob slams into $target and sends them flying!")
    }

    @Test
    fun testCanBashAMobInAFight() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createPlayerMob {
            it.skills[SkillType.BASH] = 100
        }
        val target = test.createMob()

        test.addFight(mob, target)

        // when
        val response = test.runActionForIOStatus(mob, "bash", IOStatus.OK) {
            mob.mv = 100
        }

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you slam into $target and send them flying!")
        assertThat(response.message.toTarget).isEqualTo("$mob slams into you and sends you flying!")
        assertThat(response.message.toObservers).isEqualTo("$mob slams into $target and sends them flying!")
    }
}

package kotlinmud.mob.skill.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isLessThan
import kotlinmud.attributes.type.Attribute
import kotlinmud.io.type.IOStatus
import kotlinmud.mob.race.impl.Bear
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.test.helper.createTestService
import kotlinmud.test.helper.getIdentifyingWord
import org.junit.Test

class BackstabTest {
    @Test
    fun testCanBackstab() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createPlayerMob {
            it.skills[SkillType.BACKSTAB] = 100
        }
        val target = test.createMob()

        // when
        val response = test.runActionForIOStatus(mob, "backstab ${getIdentifyingWord(target)}", IOStatus.OK)

        // then
        assertThat(response.message.toActionCreator).isEqualTo("You stab $target in the back.")
        assertThat(response.message.toTarget).isEqualTo("$mob stabs you in the back.")
        assertThat(response.message.toObservers).isEqualTo("$mob stabs $target in the back.")

        // and
        assertThat(target.hp).isLessThan(target.calc(Attribute.HP))
    }

    @Test
    fun testBackstabFailsOnSaveAgainstPierce() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createPlayerMob {
            it.skills[SkillType.BACKSTAB] = 1
        }
        val target = test.createMobBuilder()
            .also { it.race = Bear() }
            .build()

        // when
        val response = test.runActionForIOStatus(
            mob,
            "backstab ${getIdentifyingWord(target)}",
            IOStatus.FAILED
        ) { mob.mv = 100 }

        // then
        assertThat(response.message.toActionCreator).isEqualTo("You lost your concentration.")
    }
}

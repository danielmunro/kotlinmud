package kotlinmud.mob.skill.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isLessThan
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.type.Attribute
import kotlinmud.io.type.IOStatus
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.test.helper.createTestService
import kotlinmud.test.helper.getIdentifyingWord
import org.junit.Test

class TripTest {
    @Test
    fun testTripGeneratesAccurateMessages() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createPlayerMob {
            it.skills[SkillType.TRIP] = 100
        }
        val target = test.createMob()

        // when
        val response = test.runActionForIOStatus(mob, "trip ${getIdentifyingWord(target)}", IOStatus.OK)

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you trip $target and they go down hard.")
        assertThat(response.message.toTarget).isEqualTo("$mob trips you!")
        assertThat(response.message.toObservers).isEqualTo("$mob trips $target.")
    }

    @Test
    fun testTripImpartsDamage() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createPlayerMob {
            it.skills[SkillType.TRIP] = 100
        }
        val target = test.createMob()

        // when
        test.runActionForIOStatus(mob, "trip ${getIdentifyingWord(target)}", IOStatus.OK)

        // then
        assertThat(target.hp).isLessThan(target.calc(Attribute.HP))
    }

    @Test
    fun testTripImpartsStunnedAffect() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createPlayerMob {
            it.skills[SkillType.TRIP] = 100
        }
        val target = test.createMob()

        // when
        test.runActionForIOStatus(mob, "trip ${getIdentifyingWord(target)}", IOStatus.OK)

        // then
        assertThat(target.affects.first().type).isEqualTo(AffectType.STUNNED)
    }
}

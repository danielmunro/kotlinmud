package kotlinmud.mob.skill.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isLessThan
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.type.Attribute
import kotlinmud.io.type.IOStatus
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.test.createTestService
import kotlinmud.test.createTestServiceWithResetDB
import kotlinmud.test.getIdentifyingWord
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class TripTest {
    @Test
    fun testTripGeneratesAccurateMessages() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createMob {
            it.skills[SkillType.TRIP] = 100
        }

        // when
        val response = test.runActionForIOStatus(mob, "trip ${getIdentifyingWord(test.createMob())}", IOStatus.OK)

        // then
        val target = test.getTarget()
        assertThat(response.message.toActionCreator).isEqualTo("you trip $target and they go down hard.")
        assertThat(response.message.toTarget).isEqualTo("$mob trips you!")
        assertThat(response.message.toObservers).isEqualTo("$mob trips $target.")
    }

    @Test
    fun testTripImpartsDamage() {
        // setup
        val test = createTestServiceWithResetDB()

        // given
        val mob = test.createMob {
            it.skills[SkillType.TRIP] = 100
        }

        // when
        test.runActionForIOStatus(mob, "trip ${getIdentifyingWord(test.createMob())}", IOStatus.OK)

        // then
        test.getTarget().let {
            assertThat(it.hp).isLessThan(it.calc(Attribute.HP))
        }
    }

    @Test
    fun testTripImpartsStunnedAffect() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createMob {
            it.skills[SkillType.TRIP] = 100
        }

        // when
        test.runActionForIOStatus(mob, "trip ${getIdentifyingWord(test.createMob())}", IOStatus.OK)

        // then
        test.getTarget().let {
            assertThat(transaction { it.affects.first().type }).isEqualTo(AffectType.STUNNED)
        }
    }
}

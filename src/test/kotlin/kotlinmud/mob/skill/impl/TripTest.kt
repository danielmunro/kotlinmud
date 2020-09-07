package kotlinmud.mob.skill.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isLessThan
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.type.Attribute
import kotlinmud.io.type.IOStatus
import kotlinmud.mob.repository.findMobById
import kotlinmud.mob.skill.factory.createSkill
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
        val mob = test.createMob { createSkill(SkillType.TRIP, it, 100) }
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
        val test = createTestServiceWithResetDB()

        // given
        val mob = test.createMob { createSkill(SkillType.TRIP, it, 100) }
        val target = test.createMob()

        // when
        test.runActionForIOStatus(mob, "trip ${getIdentifyingWord(target)}", IOStatus.OK)

        // then
        with(findMobById(target.id.value)) {
            assertThat(this.hp).isLessThan(this.calc(Attribute.HP))
        }
    }

    @Test
    fun testTripImpartsStunnedAffect() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createMob { createSkill(SkillType.TRIP, it, 100) }
        val target = test.createMob()

        // when
        test.runActionForIOStatus(mob, "trip ${getIdentifyingWord(target)}", IOStatus.OK)

        // then
        assertThat(transaction { target.affects.first().type }).isEqualTo(AffectType.STUNNED)
    }
}

package kotlinmud.event.observer

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isTrue
import kotlinmud.event.factory.createKillEvent
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.type.Disposition
import kotlinmud.test.createTestService
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class GrantExperienceOnKillObserverTest {
    @Test
    fun testKillsAddExperience() {
        // setup
        val testService = createTestService()
        val mob1 = testService.createPlayerMob()
        val mob2 = testService.createPlayerMob()
        val fight = Fight(mob1, mob2)
        testService.addFight(fight)

        // given
        transaction { mob2.disposition = Disposition.DEAD }

        // when
        testService.publish(createKillEvent(fight))

        // then
        assertThat(transaction { mob1.mobCard!!.experience }).isGreaterThan(0)
        assertThat(transaction { mob1.level }).isEqualTo(1)
    }

    @Test
    fun testEnoughExpTriggersLevel() {
        // setup
        val testService = createTestService()
        val mob1 = testService.createPlayerMob()
        val mob2 = testService.createPlayerMob()
        val fight = Fight(mob1, mob2)
        testService.addFight(fight)
        transaction { mob2.disposition = Disposition.DEAD }
        val mobCard1 = testService.findMobCardByName(mob1.name)!!

        // given
        transaction { mobCard1.experience = 2000 }
        testService.publish(createKillEvent(fight))
        val addExperience = mobCard1.addExperience(mob1.level, 1)

        // then
        assertThat(addExperience.levelGained).isTrue()
    }
}

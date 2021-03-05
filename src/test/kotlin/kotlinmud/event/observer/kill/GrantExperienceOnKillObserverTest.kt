package kotlinmud.event.observer.kill

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isTrue
import kotlinmud.mob.type.Disposition
import kotlinmud.test.helper.createTestService
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class GrantExperienceOnKillObserverTest {
    @Test
    fun testKillsAddExperience() {
        // setup
        val testService = createTestService()
        val mob1 = testService.createPlayerMob()
        val mob2 = testService.createPlayerMob()

        // given
        mob2.disposition = Disposition.DEAD

        // when
        val fight = testService.addFight(mob1, mob2)
        testService.publish(fight.createKillEvent())

        // then
        assertThat(transaction { mob1.mobCard!!.experience }).isGreaterThan(0)
        assertThat(mob1.level).isEqualTo(1)
    }

    @Test
    fun testEnoughExpTriggersLevel() {
        // setup
        val testService = createTestService()
        val mob1 = testService.createPlayerMob()
        val mob2 = testService.createPlayerMob()
        val fight = testService.addFight(mob1, mob2)
        mob2.disposition = Disposition.DEAD
        val mobCard1 = testService.findMobCardByName(mob1.name)!!

        // given
        transaction { mobCard1.experience = 2000 }
        testService.publish(fight.createKillEvent())
        val addExperience = mobCard1.addExperience(mob1.level, 1)

        // then
        assertThat(addExperience.levelGained).isTrue()
    }
}

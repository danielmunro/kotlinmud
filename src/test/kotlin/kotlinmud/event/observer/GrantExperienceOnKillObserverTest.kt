package kotlinmud.event.observer

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isTrue
import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.type.Disposition
import kotlinmud.test.createTestService
import org.junit.Test

class GrantExperienceOnKillObserverTest {
    @Test
    fun testKillsAddExperience() {
        // setup
        val testService = createTestService()
        val mob1 = testService.createPlayerMobBuilder().build()
        val mob2 = testService.createPlayerMobBuilder().build()
        val fight = Fight(mob1, mob2)
        testService.addFight(fight)
        val mobCard1 = testService.findMobCardByName(mob1.name)!!

        // given
        mob2.disposition = Disposition.DEAD

        // when
        testService.publish(Event(EventType.KILL, fight))

        // then
        assertThat(mobCard1.experience).isGreaterThan(0)
        assertThat(mob1.level).isEqualTo(1)
    }

    @Test
    fun testEnoughExpTriggersLevel() {
        // setup
        val testService = createTestService()
        val mob1 = testService.createPlayerMobBuilder().build()
        val mob2 = testService.createPlayerMobBuilder().build()
        val fight = Fight(mob1, mob2)
        testService.addFight(fight)
        mob2.disposition = Disposition.DEAD
        val mobCard1 = testService.findMobCardByName(mob1.name)!!

        // given
        mobCard1.experience = 2000
        testService.publish(Event(EventType.KILL, fight))
        val addExperience = mobCard1.addExperience(mob1.level, 1)

        // then
        assertThat(addExperience.levelGained).isTrue()
    }
}

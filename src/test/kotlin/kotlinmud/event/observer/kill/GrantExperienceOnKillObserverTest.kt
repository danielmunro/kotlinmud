package kotlinmud.event.observer.kill

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isNull
import assertk.assertions.isTrue
import kotlinmud.event.factory.createKillEvent
import kotlinmud.mob.repository.findMobById
import kotlinmud.test.createTestService
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class GrantExperienceOnKillObserverTest {
    @Test
    fun testThatNPCsDoNotGetExperienceSanityTest() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createMob()
        val target = test.createMob()
        test.addFight(mob, target)

        // when
        test.getGrantExperienceOnKillObserver().event(createKillEvent(test.findFightForMob(mob)!!))

        // then
        assertThat(mob.mobCard).isNull()
        assertThat(mob.isNpc).isTrue()
    }

    @Test
    fun testThatPlayerMobsDoGetExperienceOnKill() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createPlayerMob()
        val target = test.createMob()
        test.addFight(mob, target)

        // expect
        assertThat(transaction { mob.mobCard!!.experience }).isEqualTo(1000)

        // when
        transaction { test.getGrantExperienceOnKillObserver().event(createKillEvent(test.findFightForMob(mob)!!)) }

        // then
        assertThat(transaction { mob.mobCard!!.experience }).isGreaterThan(1000)
    }

    @Test
    fun testThatPlayerMobCanLevel() {
        // setup
        val test = createTestService()
        val mob = test.createPlayerMob()
        val target = test.createMob()
        test.addFight(mob, target)

        // given
        transaction { mob.mobCard!!.experience += mob.mobCard!!.experiencePerLevel }

        // expect
        assertThat(mob.level).isEqualTo(1)

        // when
        transaction { test.getGrantExperienceOnKillObserver().event(createKillEvent(test.findFightForMob(mob)!!)) }

        // then
        assertThat(findMobById(mob.id.value).level).isGreaterThan(1)
    }
}

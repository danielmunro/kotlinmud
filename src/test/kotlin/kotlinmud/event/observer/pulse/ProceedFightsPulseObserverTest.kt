package kotlinmud.event.observer.pulse

import assertk.assertThat
import assertk.assertions.isNull
import kotlinmud.event.factory.createPulseEvent
import kotlinmud.test.createTestService
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class ProceedFightsPulseObserverTest {
    @Test
    fun testProceedFights() {
        // setup
        val test = createTestService()
        val observer = test.getProceedFightsPulseObserver()
        val mob = test.createMob()
        test.addFight(mob, test.createMob())

        // given
        transaction { mob.hp = -1 }

        // when
        observer.processEvent(createPulseEvent())

        // then
        assertThat(test.findFightForMob(mob)).isNull()
    }
}

package kotlinmud.event.observer.impl.pulse

import assertk.assertThat
import assertk.assertions.isNull
import kotlinmud.test.helper.createTestService
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ProceedFightsPulseObserverTest {
    @Test
    fun testProceedFights() {
        // setup
        val test = createTestService()
        val mob = test.createMob()
        test.addFight(mob, test.createMob())

        // given
        mob.hp = -1

        // when
        runBlocking { test.callProceedFightsEvent() }

        // then
        assertThat(test.findFightForMob(mob)).isNull()
    }
}

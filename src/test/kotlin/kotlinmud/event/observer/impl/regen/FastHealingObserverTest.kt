package kotlinmud.event.observer.impl.regen

import assertk.assertThat
import assertk.assertions.isGreaterThan
import kotlinmud.event.impl.Event
import kotlinmud.event.impl.RegenEvent
import kotlinmud.event.type.EventType
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.test.helper.createTestService
import kotlinmud.test.model.ProbabilityTest
import kotlinx.coroutines.runBlocking
import org.junit.Test

class FastHealingObserverTest {
    @Test
    fun testFastHealingIncreasesHpRegen() {
        // setup
        val test = createTestService()
        val prob = ProbabilityTest()
        val fastHealing = FastHealingObserver()

        // given
        val mob = test.createMobBuilder()
            .also { it.skills = mutableMapOf(Pair(SkillType.FAST_HEALING, 100)) }
            .build()

        // when
        while (prob.isIterating()) {
            val event = Event(EventType.REGEN, RegenEvent(mob, 0.0, 0.0, 0.0))
            runBlocking { fastHealing.invokeAsync(event) }
            prob.decrementIteration(
                event.subject.hpRegenRate > 0.0,
                event.subject.hpRegenRate == 0.0
            )
        }

        // then
        assertThat(prob.getOutcome1()).isGreaterThan(0)
        assertThat(prob.getOutcome2()).isGreaterThan(0)
    }
}

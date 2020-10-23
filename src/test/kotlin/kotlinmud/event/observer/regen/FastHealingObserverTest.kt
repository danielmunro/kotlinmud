package kotlinmud.event.observer.regen

import assertk.assertThat
import assertk.assertions.isGreaterThan
import kotlinmud.event.impl.Event
import kotlinmud.event.impl.RegenEvent
import kotlinmud.event.observer.impl.regen.FastHealingObserver
import kotlinmud.event.type.EventType
import kotlinmud.mob.skill.factory.createSkill
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.test.ProbabilityTest
import kotlinmud.test.createTestService
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
        val mob = test.createMob {
            createSkill(SkillType.FAST_HEALING, it, 100)
        }

        // when
        while (prob.isIterating()) {
            val event = Event(EventType.REGEN, RegenEvent(mob, 0.0, 0.0, 0.0))
            runBlocking { fastHealing.invokeAsync(event) }
            prob.decrementIteration(event.subject.hpRegenRate > 0.0, event.subject.hpRegenRate == 0.0)
        }

        // then
        assertThat(prob.getOutcome1()).isGreaterThan(0)
        assertThat(prob.getOutcome2()).isGreaterThan(0)
    }
}

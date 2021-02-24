package kotlinmud.event.observer.round

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import kotlinmud.mob.repository.findFightForMob
import kotlinmud.mob.skill.factory.createSkill
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.test.ProbabilityTest
import kotlinmud.test.createTestService
import kotlinx.coroutines.runBlocking
import org.junit.Test

class SecondAttackObserverTest {
    @Test
    fun testSecondAttackGetsInvoked() {
        // setup
        val test = createTestService()

        val mob = test.createMob {
            createSkill(SkillType.SECOND_ATTACK, it, 100)
        }
        val target = test.createMob()

        val fight = test.addFight(mob, target)
        val prob = ProbabilityTest()

        while (prob.isIterating() && !fight.isOver()) {
            val round = runBlocking { test.proceedFights() }
            round.forEach {
                prob.decrementIteration(
                    it.attackerAttacks.size > 1,
                    it.defenderAttacks.size > 1
                )
            }
        }

        assertThat(prob.getOutcome1()).isGreaterThan(0)
        assertThat(prob.getOutcome2()).isEqualTo(0)
    }
}

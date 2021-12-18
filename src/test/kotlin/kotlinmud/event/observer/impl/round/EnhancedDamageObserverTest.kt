package kotlinmud.event.observer.impl.round

import assertk.assertThat
import assertk.assertions.isGreaterThan
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.test.helper.createTestService
import kotlinmud.test.model.ProbabilityTest
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.math.roundToInt

class EnhancedDamageObserverTest {
    @Test
    fun testDamageIsEnhanced() {
        // setup
        val test = createTestService()
        val prob = ProbabilityTest()

        // given
        val mob = test.createMob {
            it.skills[SkillType.ENHANCED_DAMAGE] = 100
        }
        val target = test.createMob()
        val fight = test.addFight(mob, target)

        // when
        while (prob.isIterating() && !fight.isOver()) {
            runBlocking { test.proceedFights() }.forEach {
                val dam1 = it.attackerAttacks.fold(0) { acc, attack -> acc + attack.damage }
                val dam2 = it.defenderAttacks.fold(0) { acc, attack -> acc + attack.damage }
                prob.decrementIteration(
                    dam1 >= dam2,
                    dam2 >= dam1
                )
                mob.hp = 20
                target.hp = 20
            }
        }

        // then
        assertThat(prob.getOutcome1()).isGreaterThan((prob.getOutcome2() * 0.2).roundToInt())
    }
}

package kotlinmud.event.observer

import assertk.assertThat
import assertk.assertions.isGreaterThan
import kotlin.math.roundToInt
import kotlinmud.mob.repository.findFightForMob
import kotlinmud.mob.repository.findMobById
import kotlinmud.mob.skill.factory.createSkill
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.test.ProbabilityTest
import kotlinmud.test.createTestService
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class EnhancedDamageObserverTest {
    @Test
    fun testDamageIsEnhanced() {
        // setup
        val test = createTestService()
        val prob = ProbabilityTest()

        // given
        val mob = test.createMob {
            createSkill(SkillType.ENHANCED_DAMAGE, it, 100)
        }
        val target = test.createMob()
        test.addFight(mob, target)

        // when
        while (prob.isIterating() && findFightForMob(mob) != null) {
            test.proceedFights().forEach {
                val dam1 = it.attackerAttacks.fold(0) { acc, attack -> acc + attack.damage }
                val dam2 = it.defenderAttacks.fold(0) { acc, attack -> acc + attack.damage }
                prob.decrementIteration(
                    dam1 >= dam2,
                    dam2 >= dam1
                )
                transaction {
                    findMobById(mob.id.value).hp = 20
                    findMobById(target.id.value).hp = 20
                }
            }
        }

        // then
        assertThat(prob.getOutcome1()).isGreaterThan((prob.getOutcome2() * 0.8).roundToInt())
    }
}

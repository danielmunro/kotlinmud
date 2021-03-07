package kotlinmud.mob.fight

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isNotEqualTo
import kotlinmud.attributes.type.Attribute
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.mob.fight.type.AttackResult
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Disposition
import kotlinmud.test.helper.createTestService
import kotlinmud.test.model.ProbabilityTest
import kotlinx.coroutines.runBlocking
import org.junit.Test

class FightTest {
    @Test
    fun testEvasiveSkillsGetInvoked() {
        // setup
        val testService = createTestService()
        val prob = ProbabilityTest()

        // given
        val mob = testService.createMob {
            it.skills[SkillType.SHIELD_BLOCK] = 100
            it.skills[SkillType.PARRY] = 100
            it.skills[SkillType.DODGE] = 100
        }

        // when
        testService.addFight(mob, testService.createMob())

        while (prob.isIterating()) {
            val rounds = testService.proceedFights()
            val outcome1 = rounds.find { roundContainsEvadedAttack(it.attackerAttacks) }
            val outcome2 = rounds.find { roundContainsEvadedAttack(it.defenderAttacks) }
            prob.decrementIteration(outcome1 != null, outcome2 != null)
        }

        // then
        assertThat(prob.getOutcome1()).isEqualTo(0)
        assertThat(prob.getOutcome2()).isGreaterThan(0)
    }

    @Test
    fun testShieldBlockRequiresShield() {
        // setup
        val testService = createTestService()
        val prob = ProbabilityTest()
        val createMob = {
            testService.createMob {
                it.skills[SkillType.SHIELD_BLOCK] = 100
            }
        }

        // given
        val mob1 = createMob()
        val mob2 = createMob()

        // and
        mob2.equipped.add(
            testService.createItemBuilder()
                .position(Position.SHIELD)
                .type(ItemType.EQUIPMENT)
                .material(Material.IRON)
                .build()
        )

        // and
        testService.addFight(mob1, mob2)

        // when
        while (prob.isIterating()) {
            val rounds = testService.proceedFights()
            val outcome1 = rounds.find {
                roundContainsEvadedAttack(it.attackerAttacks)
            }
            val outcome2 = rounds.find {
                roundContainsEvadedAttack(it.defenderAttacks)
            }
            prob.decrementIteration(outcome1 != null, outcome2 != null)
        }

        // then
        assertThat(prob.getOutcome1()).isGreaterThan(0)
        assertThat(prob.getOutcome2()).isEqualTo(0)
    }

    @Test
    fun testParryRequiresWeapon() {
        // setup
        val testService = createTestService()
        val prob = ProbabilityTest()
        val mob1 = testService.createMob()
        val mob2 = testService.createMob {
            it.skills[SkillType.PARRY] = 100
        }

        // given
        val fight = testService.addFight(mob1, mob2)

        // when
        while (prob.isIterating() && !fight.isOver()) {
            val rounds = testService.proceedFights()
            val outcome1 = rounds.find { roundContainsEvadedAttack(it.attackerAttacks) }
            val outcome2 = rounds.find { roundContainsEvadedAttack(it.defenderAttacks) }
            prob.decrementIteration(outcome1 != null, outcome2 != null)
        }

        // then
        assertThat(prob.getOutcome1()).isGreaterThan(0)
        assertThat(prob.getOutcome2()).isEqualTo(0)
    }

    @Test
    fun testWimpyIsInvoked() {
        // setup
        val hp = 100
        val testService = createTestService()
        val createMob = {
            testService.createMob {
                it.wimpy = hp
                it.hp = hp
                it.attributes[Attribute.HIT] = 10
            }
        }

        val mob1 = createMob()
        val mob2 = createMob()
        val dst = testService.createRoom()
        testService.getStartRoom().north = dst

        // given
        val fight = testService.addFight(mob1, mob2)

        // when
        while (!fight.isOver()) {
            runBlocking { testService.proceedFights() }
            mob1.hp = hp - 1
            mob2.hp = hp - 1
        }

        // then
        assertThat(mob1.disposition).isEqualTo(Disposition.STANDING)
        assertThat(mob2.disposition).isEqualTo(Disposition.STANDING)

        // and
        assertThat(mob1.hp).isGreaterThan(0)
        assertThat(mob2.hp).isGreaterThan(0)

        // and
        assertThat(mob1.room).isNotEqualTo(mob2.room)
    }

    private fun roundContainsEvadedAttack(attacks: List<Attack>): Boolean {
        return attacks.find { it.attackResult == AttackResult.EVADE } != null
    }
}

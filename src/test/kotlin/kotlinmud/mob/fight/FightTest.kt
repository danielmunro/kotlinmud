package kotlinmud.mob.fight

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isNotEqualTo
import kotlinmud.item.type.Position
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Disposition
import kotlinmud.test.ProbabilityTest
import kotlinmud.test.createTestService
import org.junit.Test

class FightTest {
    @Test
    fun testEvasiveSkillsGetInvoked() {
        // setup
        val testService = createTestService()
        val prob = ProbabilityTest()

        // given
        val mob = testService.createMob()
        mob.addSkill(SkillType.SHIELD_BLOCK, 100)
        mob.addSkill(SkillType.PARRY, 100)
        mob.addSkill(SkillType.DODGE, 100)

        // when
        val fight = Fight(mob, testService.createMob())
        testService.addFight(fight)

        while (prob.isIterating()) {
            val round = fight.createRound()
            val outcome1 = round.attackerAttacks.find { it.attackResult == AttackResult.EVADE }
            val outcome2 = round.defenderAttacks.find { it.attackResult == AttackResult.EVADE }
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

        // given
        val mob1 = testService.createMob()
        mob1.addSkill(SkillType.SHIELD_BLOCK, 100)
        val mob2 = testService.createMob()
        mob2.addSkill(SkillType.SHIELD_BLOCK, 100)
        val item = testService.createItem(mob2)
        item.position = Position.SHIELD
        mob2.equipped.plus(item)

        // when
        val fight = Fight(mob1, mob2)
        testService.addFight(fight)

        while (prob.isIterating()) {
            val round = fight.createRound()
            val outcome1 = round.attackerAttacks.find { it.attackResult == AttackResult.EVADE }
            val outcome2 = round.defenderAttacks.find { it.attackResult == AttackResult.EVADE }
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

        // given
        val mob1 = testService.createMob()
        mob1.addSkill(SkillType.PARRY, 100)
        mob1.equipped.minus(mob1.equipped.first())

        val mob2 = testService.createMob()
        mob2.addSkill(SkillType.PARRY, 100)

        val item = testService.createItem(mob2)
        item.position = Position.WEAPON

        val fight = Fight(mob1, mob2)

        testService.addFight(fight)

        // when
        while (prob.isIterating()) {
            val round = fight.createRound()
            val outcome1 = round.attackerAttacks.find { it.attackResult == AttackResult.EVADE }
            val outcome2 = round.defenderAttacks.find { it.attackResult == AttackResult.EVADE }
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
        val mob1 = testService.createMob()
        mob1.hp = hp
        mob1.wimpy = hp
        val mob2 = testService.createMob()
        mob2.hp = hp
        mob2.wimpy = hp
        val fight = Fight(mob1, mob2)

        // given
        testService.addFight(fight)

        // when
        while (!fight.isOver()) {
            testService.proceedFights()
        }

        // then
        assertThat(mob1.disposition).isEqualTo(Disposition.STANDING)
        assertThat(mob2.disposition).isEqualTo(Disposition.STANDING)

        // and
        assertThat(mob1.hp).isGreaterThan(0)
        assertThat(mob2.hp).isGreaterThan(0)

        // and
        val room1 = testService.getRoomForMob(mob1)
        val room2 = testService.getRoomForMob(mob2)
        assertThat(room1.id).isNotEqualTo(room2.id)
    }
}

package kotlinmud.mob.fight

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import kotlinmud.item.Position
import kotlinmud.mob.skill.SkillType
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
        val mob = testService.buildMob(testService.mobBuilder()
            .addSkill(SkillType.SHIELD_BLOCK, 100)
            .addSkill(SkillType.PARRY, 100)
            .addSkill(SkillType.DODGE, 100)
        )

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
        val mob1 = testService.buildMob(testService.mobBuilder()
            .addSkill(SkillType.SHIELD_BLOCK, 100)
        )

        val mob2 = testService.buildMob(testService.mobBuilder()
            .addSkill(SkillType.SHIELD_BLOCK, 100)
            .equip(testService.buildItem(testService.itemBuilder()
                .setPosition(Position.SHIELD))))

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
        val mob1 = testService.buildMob(testService.mobBuilder()
            .addSkill(SkillType.PARRY, 100)
        )
        mob1.equipped.items.removeAt(0)

        val mob2 = testService.buildMob(testService.mobBuilder()
            .addSkill(SkillType.PARRY, 100)
            .equip(testService.buildItem(testService.itemBuilder()
                .setPosition(Position.WEAPON))))

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
}

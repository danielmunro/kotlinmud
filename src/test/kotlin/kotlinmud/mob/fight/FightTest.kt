package kotlinmud.mob.fight

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isNotEqualTo
import kotlinmud.item.type.Position
import kotlinmud.mob.fight.type.AttackResult
import kotlinmud.mob.skill.factory.createSkill
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Disposition
import kotlinmud.test.ProbabilityTest
import kotlinmud.test.createTestService
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class FightTest {
    @Test
    fun testEvasiveSkillsGetInvoked() {
        // setup
        val testService = createTestService()
        val prob = ProbabilityTest()

        // given
        val mob = testService.createMob()
        createSkill(SkillType.SHIELD_BLOCK, mob, 100)
        createSkill(SkillType.PARRY, mob, 100)
        createSkill(SkillType.DODGE, mob, 100)

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
        val mob2 = testService.createMob()
        createSkill(SkillType.SHIELD_BLOCK, mob1, 100)
        createSkill(SkillType.SHIELD_BLOCK, mob2, 100)

        // and
        val item = testService.createItem()
        transaction {
            item.mobInventory = mob2
            item.position = Position.SHIELD
            item.mobEquipped = mob2
        }

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
        createSkill(SkillType.PARRY, mob1, 100)
        transaction {
            mob1.equipped.forEach {
                it.mobEquipped = null
            }
        }

        val mob2 = testService.createMob()
        createSkill(SkillType.PARRY, mob2, 100)

        val fight = Fight(mob1, mob2)

        testService.addFight(fight)

        // when
        while (prob.isIterating() && !fight.isOver()) {
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
        val mob2 = testService.createMob()
        val dst = testService.createRoom()
        transaction { testService.getStartRoom().north = dst }

        // given
        transaction {
            mob1.hp = hp
            mob1.wimpy = hp
            mob1.attributes.hit = 10
            mob2.hp = hp
            mob2.wimpy = hp
            mob2.attributes.hit = 10
        }

        // and
        val fight = Fight(mob1, mob2)
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

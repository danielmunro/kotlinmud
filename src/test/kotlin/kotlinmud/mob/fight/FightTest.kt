package kotlinmud.mob.fight

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isNotEqualTo
import kotlinmud.item.InventoryBuilder
import kotlinmud.item.Position
import kotlinmud.mob.Disposition
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
            .skills(
                mutableMapOf(
                    Pair(SkillType.SHIELD_BLOCK, 100),
                    Pair(SkillType.PARRY, 100),
                    Pair(SkillType.DODGE, 100)
                )
            )
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
            .skills(mutableMapOf(Pair(SkillType.SHIELD_BLOCK, 100)))
        )

        val mob2 = testService.buildMob(testService.mobBuilder()
            .skills(mutableMapOf(Pair(SkillType.SHIELD_BLOCK, 100)))
            .equipped(
                InventoryBuilder().items(
                    mutableListOf(testService.buildItem(testService.itemBuilder().position(Position.SHIELD)))
                ).build()
            )
        )

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
            .skills(mutableMapOf(Pair(SkillType.PARRY, 100)))
        )
        mob1.equipped.items.removeAt(0)

        val mob2 = testService.buildMob(testService.mobBuilder()
            .skills(mutableMapOf(Pair(SkillType.PARRY, 100)))
            .equipped(
                InventoryBuilder()
                    .items(
                        mutableListOf(testService.buildItem(testService.itemBuilder()
                            .position(Position.WEAPON)))
                    ).build()
                )
        )

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
        val mob1 = testService.buildMob(testService.mobBuilder().hp(hp).wimpy(hp))
        val mob2 = testService.buildMob(testService.mobBuilder().hp(hp).wimpy(hp))
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

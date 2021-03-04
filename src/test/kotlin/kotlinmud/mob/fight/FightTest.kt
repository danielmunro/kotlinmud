package kotlinmud.mob.fight

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.mob.fight.type.AttackResult
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.test.helper.createTestService
import kotlinmud.test.model.ProbabilityTest
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
            val outcome1 = rounds.find { round ->
                round.attackerAttacks.find {
                    it.attackResult == AttackResult.EVADE
                } != null
            }
            val outcome2 = rounds.find { round ->
                round.defenderAttacks.find {
                    it.attackResult == AttackResult.EVADE
                } != null
            }
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
        var invoked = false

        // given
        val mob1 = testService.createMob {
            it.skills[SkillType.SHIELD_BLOCK] = 100
        }
        val mob2 = testService.createMob {
            it.skills[SkillType.SHIELD_BLOCK] = 100
        }

        // and
        val item = testService.createItemBuilder()
            .position(Position.SHIELD)
            .type(ItemType.EQUIPMENT)
            .material(Material.IRON)
            .build()
        mob2.equipped.add(item)

        // when
        testService.addFight(mob1, mob2)

        while (!invoked && prob.isIterating()) {
            val rounds = testService.proceedFights()
            val outcome1 = rounds.find { round ->
                round.attackerAttacks.find {
                    it.attackResult == AttackResult.EVADE
                } != null
            }
            val outcome2 = rounds.find { round ->
                round.defenderAttacks.find {
                    it.attackResult == AttackResult.EVADE
                } != null
            }
            prob.decrementIteration(outcome1 != null, outcome2 != null)
            invoked = outcome1 != null || outcome2 != null
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
        val mob1 = testService.createMob {
            it.skills[SkillType.PARRY] = 100
        }
        mob1.equipped.clear()

        val mob2 = testService.createMob {
            it.skills[SkillType.PARRY] = 100
        }

        val fight = testService.addFight(mob1, mob2)

        // when
        while (prob.isIterating() && !fight.isOver()) {
            val rounds = testService.proceedFights()
            val outcome1 = rounds.find { round ->
                round.attackerAttacks.find {
                    it.attackResult == AttackResult.EVADE
                } != null
            }
            val outcome2 = rounds.find { round ->
                round.defenderAttacks.find {
                    it.attackResult == AttackResult.EVADE
                } != null
            }
            prob.decrementIteration(outcome1 != null, outcome2 != null)
        }

        // then
        assertThat(prob.getOutcome1()).isGreaterThan(0)
        assertThat(prob.getOutcome2()).isEqualTo(0)
    }

    @Test
    fun testWimpyIsInvoked() {
//        // setup
//        val hp = 100
//        val testService = createTestServiceWithResetDB()
//        val room = testService.getStartRoom()
//        val mob1 = testService.createMob {
//            it.wimpy = hp
//            it.attributes.hit = 10
//            it.room = room
//        }
//        val mob2 = testService.createMob {
//            it.wimpy = 0
//            it.attributes.hit = 10
//            it.room = room
//        }
//        val dst = testService.createRoom()
//        transaction { testService.getStartRoom().north = dst }
//
//        // given
//        val fight = testService.addFight(mob1, mob2)
//
//        // when
//        while (!fight.isOver()) {
//            testService.proceedFights()
//        }
//
//        // then
//        assertThat(mob1.disposition).isEqualTo(Disposition.STANDING)
//        assertThat(mob2.disposition).isEqualTo(Disposition.STANDING)
//
//        // and
//        assertThat(mob1.hp).isGreaterThan(0)
//        assertThat(mob2.hp).isGreaterThan(0)
//
//        // and
//        assertThat(mob1.room).isNotEqualTo(mob2.room)
    }
}

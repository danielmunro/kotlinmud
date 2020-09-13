package kotlinmud.mob.fight

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isNotEqualTo
import kotlinmud.item.type.Position
import kotlinmud.mob.fight.type.AttackResult
import kotlinmud.mob.repository.findFightForMob
import kotlinmud.mob.repository.findMobById
import kotlinmud.mob.skill.factory.createSkill
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Disposition
import kotlinmud.room.repository.findRoomByMobId
import kotlinmud.test.ProbabilityTest
import kotlinmud.test.createTestServiceWithResetDB
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class FightTest {
    @Test
    fun testEvasiveSkillsGetInvoked() {
        // setup
        val testService = createTestServiceWithResetDB()
        val prob = ProbabilityTest()

        // given
        val mob = testService.createMob()
        createSkill(SkillType.SHIELD_BLOCK, mob, 100)
        createSkill(SkillType.PARRY, mob, 100)
        createSkill(SkillType.DODGE, mob, 100)

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
        val testService = createTestServiceWithResetDB()
        val prob = ProbabilityTest()
        var invoked = false

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
        val testService = createTestServiceWithResetDB()
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
        // setup
        val hp = 100
        val testService = createTestServiceWithResetDB()
        val room = testService.getStartRoom()
        val mob1 = testService.createMob {
            it.wimpy = hp
            it.attributes.hit = 10
            it.room = room
        }
        val mob2 = testService.createMob {
            it.wimpy = 0
            it.attributes.hit = 10
            it.room = room
        }
        val dst = testService.createRoom()
        transaction { testService.getStartRoom().north = dst }

        // given
        testService.addFight(mob1, mob2)

        // when
        while (findFightForMob(mob1) != null) {
            testService.proceedFights()
        }

        // then
        findMobById(mob1.id.value).let { assertThat(it.disposition).isEqualTo(Disposition.STANDING) }
        findMobById(mob2.id.value).let { assertThat(it.disposition).isEqualTo(Disposition.STANDING) }

        // and
        assertThat(mob1.hp).isGreaterThan(0)
        assertThat(mob2.hp).isGreaterThan(0)

        // and
        val room1 = transaction { findRoomByMobId(mob1.id.value) }
        val room2 = transaction { findRoomByMobId(mob2.id.value) }
        assertThat(room1).isNotEqualTo(room2)
    }
}

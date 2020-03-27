package kotlinmud.mob

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isLessThan
import kotlin.test.assertEquals
import kotlinmud.affect.AffectType
import kotlinmud.affect.affects
import kotlinmud.attributes.Attribute
import kotlinmud.attributes.Attributes
import kotlinmud.item.Position
import kotlinmud.mob.fight.DamageType
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.race.impl.Elf
import kotlinmud.mob.race.impl.Faerie
import kotlinmud.mob.race.impl.Goblin
import kotlinmud.mob.race.impl.Ogre
import kotlinmud.test.ProbabilityTest
import kotlinmud.test.createTestService
import org.junit.Test

class MobTest {
    @Test
    fun testEquipmentIncreasesAttributes() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // given
        val initialHp = mob.calc(Attribute.HP)
        val initialMana = mob.calc(Attribute.MANA)
        val initialMv = mob.calc(Attribute.MV)
        val initialStat = mob.calc(Attribute.STR)
        val initialHit = mob.calc(Attribute.HIT)
        val initialDam = mob.calc(Attribute.DAM)
        val initialAc = mob.calc(Attribute.AC_BASH)

        // when
        val item = testService.itemBuilder().attributes(
            Attributes.Builder()
                .setHp(1)
                .setMana(1)
                .setMv(1)
                .setStr(1)
                .setInt(1)
                .setWis(1)
                .setDex(1)
                .setCon(1)
                .setHit(1)
                .setDam(1)
                .setAcBash(1)
                .setAcSlash(1)
                .setAcPierce(1)
                .setAcMagic(1)
                .build()
        )
        mob.equipped.items.add(item.build())

        // then
        assertEquals(initialHp + 1, mob.calc(Attribute.HP))
        assertEquals(initialMana + 1, mob.calc(Attribute.MANA))
        assertEquals(initialMv + 1, mob.calc(Attribute.MV))
        assertEquals(initialStat + 1, mob.calc(Attribute.STR))
        assertEquals(initialStat + 1, mob.calc(Attribute.INT))
        assertEquals(initialStat + 1, mob.calc(Attribute.WIS))
        assertEquals(initialStat + 1, mob.calc(Attribute.DEX))
        assertEquals(initialStat + 1, mob.calc(Attribute.CON))
        assertEquals(initialHit + 1, mob.calc(Attribute.HIT))
        assertEquals(initialDam + 1, mob.calc(Attribute.DAM))
        assertEquals(initialAc + 1, mob.calc(Attribute.AC_BASH))
        assertEquals(initialAc + 1, mob.calc(Attribute.AC_SLASH))
        assertEquals(initialAc + 1, mob.calc(Attribute.AC_PIERCE))
        assertEquals(initialAc + 1, mob.calc(Attribute.AC_MAGIC))
    }

    @Test
    fun testElfSaveBonus() {
        // setup
        val testService = createTestService()
        val mob1 = testService.createMob()
        val prob = ProbabilityTest()

        // given
        val mob2 = testService.buildMob(testService.mobBuilder().race(Elf()))

        // when
        prob.test({ mob1.savesAgainst(DamageType.NONE) }, { mob2.savesAgainst(DamageType.NONE) })

        // then
        assertThat(prob.getOutcome1()).isLessThan(prob.getOutcome2())
    }

    @Test
    fun testCurseSavePenalty() {
        // setup
        val testService = createTestService()
        val mob1 = testService.createMob()
        val prob = ProbabilityTest()

        // given
        val mob2 = testService.buildMob(testService.mobBuilder().affects(affects(AffectType.CURSE)))

        // when
        while (prob.isIterating()) {
            prob.decrementIteration(mob1.savesAgainst(DamageType.NONE), mob2.savesAgainst(DamageType.NONE))
        }

        // then
        assertThat(prob.getOutcome1()).isGreaterThan(prob.getOutcome2())
    }

    @Test
    fun testBerserkSaveBonus() {
        // setup
        val testService = createTestService()
        val mob1 = testService.buildMob(testService.mobBuilder().level(50))
        val prob = ProbabilityTest()

        // given
        val mob2 = testService.buildMob(testService
            .mobBuilder()
            .level(50)
            .affects(affects(AffectType.BERSERK)))

        // when
        while (prob.isIterating()) {
            prob.decrementIteration(mob1.savesAgainst(DamageType.NONE), mob2.savesAgainst(DamageType.NONE))
        }

        // then
        assertThat(prob.getOutcome1()).isLessThan(prob.getOutcome2())
    }

    @Test
    fun testWisIntSaveBonus() {
        // setup
        val testService = createTestService()
        val prob = ProbabilityTest()

        // given
        val mob1 = testService.buildMob(testService.mobBuilder().race(Faerie()))
        val mob2 = testService.buildMob(testService.mobBuilder().race(Ogre()))

        // when
        while (prob.isIterating()) {
            prob.decrementIteration(mob1.savesAgainst(DamageType.NONE), mob2.savesAgainst(DamageType.NONE))
        }

        // then
        assertThat(prob.getOutcome1()).isGreaterThan(prob.getOutcome2())
    }

    @Test
    fun testFightingReducesSaves() {
        // setup
        val testService = createTestService()
        val prob = ProbabilityTest()

        // given
        val mob1 = testService.createMob()
        val mob2 = testService.buildMob(testService.mobBuilder().disposition(Disposition.FIGHTING))

        // when
        while (prob.isIterating()) {
            prob.decrementIteration(mob1.savesAgainst(DamageType.NONE), mob2.savesAgainst(DamageType.NONE))
        }

        // then
        assertThat(prob.getOutcome1()).isGreaterThan(prob.getOutcome2())
    }

    @Test
    fun testImmuneSaves() {
        // setup
        val testService = createTestService()
        val prob = ProbabilityTest()

        // given
        val mob1 = testService.buildMob(testService.mobBuilder().race(Goblin()))
        val mob2 = testService.buildMob(testService.mobBuilder().race(Ogre()))

        // when
        while (prob.isIterating()) {
            prob.decrementIteration(mob1.savesAgainst(DamageType.POISON), mob2.savesAgainst(DamageType.POISON))
        }

        // then
        assertThat(prob.getOutcome1()).isEqualTo(1000)
        assertThat(prob.getOutcome2()).isLessThan(1000)
    }

    @Test
    fun testMageSaveBonus() {
        // setup
        val testService = createTestService()
        val prob = ProbabilityTest()

        // given
        val mob1 = testService.buildMob(testService.mobBuilder().specialization(SpecializationType.MAGE))
        val mob2 = testService.createMob()

        // when
        while (prob.isIterating()) {
            prob.decrementIteration(mob1.savesAgainst(DamageType.NONE), mob2.savesAgainst(DamageType.NONE))
        }

        // then
        assertThat(prob.getOutcome1()).isGreaterThan(prob.getOutcome2())
    }

    @Test
    fun testClericSaveBonus() {
        // setup
        val testService = createTestService()
        val prob = ProbabilityTest(10000)

        // given
        val mob1 = testService.buildMob(testService.mobBuilder().specialization(SpecializationType.CLERIC))
        val mob2 = testService.createMob()

        // when
        while (prob.isIterating()) {
            prob.decrementIteration(mob1.savesAgainst(DamageType.NONE), mob2.savesAgainst(DamageType.NONE))
        }

        // then
        assertThat(prob.getOutcome1()).isGreaterThan(prob.getOutcome2())
    }

    @Test
    fun testEquipItemIncreasesAttribute() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // given
        mob.equipped.items.add(
            testService.buildItem(
                testService.itemBuilder()
                    .position(Position.SHIELD)
                    .attributes(
                        Attributes.Builder()
                            .setHp(100)
                            .build()
                    )
            )
        )

        // expect
        assertThat(mob.calc(Attribute.HP)).isEqualTo(120)
    }

    @Test
    fun testGoldTransfersWhenMobIsKilled() {
        // setup
        val testService = createTestService()

        // given
        val mob1 = testService.buildMob(testService.mobBuilder().gold(5).hp(1))
        val mob2 = testService.buildMob(testService.mobBuilder().gold(5).hp(1))
        val fight = Fight(mob1, mob2)
        testService.addFight(fight)

        // when
        while (!fight.isOver()) {
            testService.proceedFights()
        }

        // then
        val winner = fight.getWinner()!!
        assertThat(winner.gold).isEqualTo(10)
        assertThat(fight.getOpponentFor(winner)!!.gold).isEqualTo(0)
    }

    @Test
    fun testLoadMobWithAffects() {
        // setup
        val test = createTestService()

        test.respawnWorld()

        val mob = test.getMobRooms().find { it.mob.id == 1 }!!.mob

        assertThat(mob.affects().getAffects()).hasSize(2)
    }
}

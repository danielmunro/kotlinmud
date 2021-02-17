package kotlinmud.mob

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isGreaterThanOrEqualTo
import assertk.assertions.isLessThan
import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.type.Attribute
import kotlinmud.event.factory.createKillEvent
import kotlinmud.item.type.Position
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.impl.Elf
import kotlinmud.mob.race.impl.Faerie
import kotlinmud.mob.race.impl.Goblin
import kotlinmud.mob.race.impl.Ogre
import kotlinmud.mob.repository.findMobById
import kotlinmud.mob.specialization.type.SpecializationType
import kotlinmud.mob.type.CurrencyType
import kotlinmud.mob.type.Disposition
import kotlinmud.test.ProbabilityTest
import kotlinmud.test.createTestService
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test
import kotlin.math.roundToInt
import kotlin.test.assertEquals

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
        testService.createItem {
            it.attributes = AttributesDAO.new {
                hp = 1
                mana = 1
                mv = 1
                strength = 1
                intelligence = 1
                wisdom = 1
                dexterity = 1
                constitution = 1
                hit = 1
                dam = 1
                acBash = 1
                acSlash = 1
                acPierce = 1
                acMagic = 1
            }
            it.mobEquipped = mob
        }

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
        val mob2 = testService.createMob { it.race = Elf() }

        // when
        prob.test(
            { mob1.savesAgainst(DamageType.NONE) },
            { mob2.savesAgainst(DamageType.NONE) }
        )

        // then
        assertThat(prob.getOutcome1() * 0.75).isLessThan(prob.getOutcome2() * 1.25)
    }

    @Test
    fun testCurseSavePenalty() {
        // setup
        val testService = createTestService()
        val mob1 = testService.createMob()
        val prob = ProbabilityTest()
        val affect = createAffect(AffectType.CURSE)

        // given
        val mob2 = testService.createMob { affect.mob = it }

        // when
        while (prob.isIterating()) {
            prob.decrementIteration(
                mob1.savesAgainst(DamageType.NONE),
                mob2.savesAgainst(DamageType.NONE)
            )
        }

        // then
        assertThat(prob.getOutcome1()).isGreaterThanOrEqualTo((prob.getOutcome2() * 0.5).roundToInt())
    }

    @Test
    fun testBerserkSaveBonus() {
        // setup
        val testService = createTestService()
        val affect = createAffect(AffectType.BERSERK)
        val prob = ProbabilityTest()

        // given
        val mob1 = testService.createMob { it.level = 50 }
        val mob2 = testService.createMob {
            it.level = 50
            affect.mob = it
        }

        // when
        while (prob.isIterating()) {
            prob.decrementIteration(
                mob1.savesAgainst(DamageType.NONE),
                mob2.savesAgainst(DamageType.NONE)
            )
        }

        // then
        assertThat(prob.getOutcome1()).isLessThan((prob.getOutcome2() * 1.5).roundToInt())
    }

    @Test
    fun testWisIntSaveBonus() {
        // setup
        val testService = createTestService()
        val prob = ProbabilityTest()

        // given
        val mob1 = testService.createMob { it.race = Faerie() }
        val mob2 = testService.createMob { it.race = Ogre() }

        // when
        while (prob.isIterating()) {
            prob.decrementIteration(
                mob1.savesAgainst(DamageType.NONE),
                mob2.savesAgainst(DamageType.NONE)
            )
        }

        // then
        assertThat(prob.getOutcome1()).isGreaterThan(prob.getOutcome2() / 2)
    }

    @Test
    fun testFightingReducesSaves() {
        // setup
        val testService = createTestService()
        val prob = ProbabilityTest()

        // given
        val mob1 = testService.createMob()
        val mob2 = testService.createMob { it.disposition = Disposition.FIGHTING }

        // when
        while (prob.isIterating()) {
            prob.decrementIteration(
                mob1.savesAgainst(DamageType.NONE),
                mob2.savesAgainst(DamageType.NONE)
            )
        }

        // then
        assertThat(prob.getOutcome1()).isGreaterThan((prob.getOutcome2() * 0.5).roundToInt())
    }

    @Test
    fun testImmuneSaves() {
        // setup
        val testService = createTestService()
        val prob = ProbabilityTest()

        // given
        val mob1 = testService.createMob()
        val mob2 = testService.createMob()
        transaction {
            mob1.race = Goblin()
            mob2.race = Ogre()
        }

        // when
        while (prob.isIterating()) {
            prob.decrementIteration(
                mob1.savesAgainst(DamageType.POISON),
                mob2.savesAgainst(DamageType.POISON)
            )
        }

        // then
        assertThat(prob.getOutcome1()).isEqualTo(prob.totalIterations)
        assertThat(prob.getOutcome2()).isLessThan(prob.totalIterations)
    }

    @Test
    fun testMageSaveBonus() {
        // setup
        val testService = createTestService()
        val prob = ProbabilityTest()
        val mob1 = testService.createMob()
        val mob2 = testService.createMob()

        // given
        transaction { mob1.specialization = SpecializationType.MAGE }

        // when
        while (prob.isIterating()) {
            prob.decrementIteration(
                mob1.savesAgainst(DamageType.NONE),
                mob2.savesAgainst(DamageType.NONE)
            )
        }

        // then
        assertThat(prob.getOutcome1()).isGreaterThanOrEqualTo((prob.getOutcome2() * 0.5).roundToInt())
    }

    @Test
    fun testClericSaveBonus() {
        // setup
        val testService = createTestService()
        val prob = ProbabilityTest()
        val mob1 = testService.createMob()
        val mob2 = testService.createMob()

        // given
        transaction { mob1.specialization = SpecializationType.CLERIC }

        // when
        while (prob.isIterating()) {
            prob.decrementIteration(
                mob1.savesAgainst(DamageType.NONE),
                mob2.savesAgainst(DamageType.NONE)
            )
        }

        // then
        assertThat(prob.getOutcome1()).isGreaterThan(prob.getOutcome2() / 2)
    }

    @Test
    fun testEquipItemIncreasesAttribute() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()

        // given
        testService.createItem {
            it.mobInventory = mob
            it.position = Position.SHIELD
            it.attributes?.hp = 100
            it.mobEquipped = mob
        }

        // expect
        assertThat(mob.calc(Attribute.HP)).isEqualTo(120)
    }

    @Test
    fun testCurrencyTransfersWhenMobIsKilled() {
        // setup
        val testService = createTestService()

        // given
        val mob1 = testService.createMob()
        val mob2 = testService.createMob {
            it.addCurrency(CurrencyType.Gold, 5)
            it.addCurrency(CurrencyType.Silver, 23)
            it.addCurrency(CurrencyType.Copper, 231)
        }

        // and
        val fight = testService.addFight(mob1, mob2)
        transaction { mob2.disposition = Disposition.DEAD }

        // when
        testService.publish(fight.createKillEvent())

        // then
        findMobById(mob1.id.value).let {
            assertThat(it.getCurrency(CurrencyType.Gold)).isEqualTo(5)
            assertThat(it.getCurrency(CurrencyType.Silver)).isEqualTo(23)
            assertThat(it.getCurrency(CurrencyType.Copper)).isEqualTo(231)
        }
        findMobById(mob2.id.value).let {
            assertThat(it.getCurrency(CurrencyType.Gold)).isEqualTo(0)
            assertThat(it.getCurrency(CurrencyType.Silver)).isEqualTo(0)
            assertThat(it.getCurrency(CurrencyType.Copper)).isEqualTo(0)
        }
    }

    @Test
    fun testCreateCorpseTransfersInventoryAndEquipment() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createMob()
        test.createItem { it.mobInventory = mob }
        test.createItem {
            it.mobInventory = mob
            it.mobEquipped = mob
        }
        val inventoryAmount = test.countItemsFor(mob)

        // when
        val corpse = test.createCorpseFrom(mob)

        // then
        assertThat(test.countItemsFor(corpse)).isEqualTo(inventoryAmount)
        assertThat(transaction { mob.equipped.toList() }).hasSize(0)
        assertThat(test.countItemsFor(mob)).isEqualTo(0)
    }
}

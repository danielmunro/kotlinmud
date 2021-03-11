package kotlinmud.mob

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThanOrEqualTo
import assertk.assertions.isLessThan
import assertk.assertions.isLessThanOrEqualTo
import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.constant.startingHp
import kotlinmud.attributes.type.Attribute
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.impl.Elf
import kotlinmud.mob.race.impl.Faerie
import kotlinmud.mob.race.impl.Goblin
import kotlinmud.mob.race.impl.Ogre
import kotlinmud.mob.specialization.impl.Cleric
import kotlinmud.mob.specialization.impl.Mage
import kotlinmud.mob.type.CurrencyType
import kotlinmud.mob.type.Disposition
import kotlinmud.test.helper.createTestService
import kotlinmud.test.model.ProbabilityTest
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
        val item = testService.createItemBuilder()
            .type(ItemType.EQUIPMENT)
            .material(Material.IRON)
            .attributes(
                mapOf(
                    Pair(Attribute.HP, 1),
                    Pair(Attribute.MANA, 1),
                    Pair(Attribute.MV, 1),
                    Pair(Attribute.STR, 1),
                    Pair(Attribute.INT, 1),
                    Pair(Attribute.WIS, 1),
                    Pair(Attribute.DEX, 1),
                    Pair(Attribute.CON, 1),
                    Pair(Attribute.HIT, 1),
                    Pair(Attribute.DAM, 1),
                    Pair(Attribute.AC_BASH, 1),
                    Pair(Attribute.AC_SLASH, 1),
                    Pair(Attribute.AC_PIERCE, 1),
                    Pair(Attribute.AC_MAGIC, 1),
                )
            ).build()
        mob.equipped.add(item)

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
        val mob2 = testService
            .createMobBuilder()
            .also { it.race = Elf() }
            .build()

        // when
        prob.test(
            { mob1.savesAgainst(DamageType.NONE) },
            { mob2.savesAgainst(DamageType.NONE) }
        )

        // then
        assertThat(prob.getOutcome1() * 0.50).isLessThan(prob.getOutcome2() * 1.25)
    }

    @Test
    fun testCurseSavePenalty() {
        // setup
        val testService = createTestService()
        val mob1 = testService.createMob()
        val prob = ProbabilityTest()
        val affect = createAffect(AffectType.CURSE)

        // given
        val mob2 = testService.createMobBuilder()
            .also { it.affects = mutableListOf(affect) }
            .build()

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
        }
        mob2.affects.add(affect)

        // when
        while (prob.isIterating()) {
            prob.decrementIteration(
                mob1.savesAgainst(DamageType.NONE),
                mob2.savesAgainst(DamageType.NONE)
            )
        }

        // then
        assertThat(prob.getOutcome1()).isLessThanOrEqualTo(prob.getOutcome2() * 3)
    }

    @Test
    fun testWisIntSaveBonus() {
        // setup
        val testService = createTestService()
        val prob = ProbabilityTest()

        // given
        val mob1 = testService
            .createMobBuilder()
            .also { it.race = Faerie() }
            .build()
        val mob2 = testService
            .createMobBuilder()
            .also { it.race = Ogre() }
            .build()

        // when
        while (prob.isIterating()) {
            prob.decrementIteration(
                mob1.savesAgainst(DamageType.NONE),
                mob2.savesAgainst(DamageType.NONE)
            )
        }

        // then
        assertThat(prob.getOutcome1()).isGreaterThanOrEqualTo(prob.getOutcome2() / 2)
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
        assertThat(prob.getOutcome1()).isGreaterThanOrEqualTo(prob.getOutcome2() / 3)
    }

    @Test
    fun testImmuneSaves() {
        // setup
        val testService = createTestService()
        val prob = ProbabilityTest()

        // given
        val mob1 = testService
            .createMobBuilder()
            .also { it.race = Goblin() }
            .build()
        val mob2 = testService
            .createMobBuilder()
            .also { it.race = Ogre() }
            .build()

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

        // given
        val mob1 = testService
            .createMobBuilder()
            .also { it.specialization = Mage() }
            .build()
        val mob2 = testService.createMob()

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

        // given
        val mob1 = testService
            .createMobBuilder()
            .also { it.specialization = Cleric() }
            .build()
        val mob2 = testService.createMob()

        // when
        while (prob.isIterating()) {
            prob.decrementIteration(
                mob1.savesAgainst(DamageType.NONE),
                mob2.savesAgainst(DamageType.NONE)
            )
        }

        // then
        assertThat(prob.getOutcome1()).isGreaterThanOrEqualTo(prob.getOutcome2() / 2)
    }

    @Test
    fun testEquipItemIncreasesAttribute() {
        // setup
        val testService = createTestService()
        val mob = testService.createMob()
        val baseHp = startingHp
        val bonusHp = 100

        // given
        mob.equipped.add(
            testService.createItemBuilder()
                .position(Position.SHIELD)
                .attributes(
                    mapOf(
                        Pair(Attribute.HP, bonusHp)
                    )
                )
                .type(ItemType.EQUIPMENT)
                .material(Material.IRON)
                .build()
        )

        // expect
        assertThat(mob.calc(Attribute.HP)).isEqualTo(baseHp + bonusHp)
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
        mob2.disposition = Disposition.DEAD

        // when
        testService.publish(fight.createKillEvent())

        // then
        mob1.let {
            assertThat(it.getCurrency(CurrencyType.Gold)).isEqualTo(5)
            assertThat(it.getCurrency(CurrencyType.Silver)).isEqualTo(23)
            assertThat(it.getCurrency(CurrencyType.Copper)).isEqualTo(231)
        }
        mob2.let {
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
        mob.items.add(test.createItem())
        mob.equipped.add(test.createItem())
        val amount = mob.items.size + mob.equipped.size

        // when
        val corpse = test.createCorpseFrom(mob)

        // then
        assertThat(corpse.items!!).hasSize(amount)
        assertThat(mob.equipped).hasSize(0)
        assertThat(mob.items).hasSize(0)
    }
}

package kotlinmud.mob

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isLessThan
import com.tylerthrailkill.helpers.prettyprint.pp
import java.lang.StringBuilder
import kotlin.test.assertEquals
import kotlinmud.affect.factory.affects
import kotlinmud.affect.model.AffectInstance
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.model.AttributesBuilder
import kotlinmud.attributes.type.Attribute
import kotlinmud.fs.loader.Tokenizer
import kotlinmud.item.type.Position
import kotlinmud.mob.fight.DamageType
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.loader.MobLoader
import kotlinmud.mob.mapper.mapMob
import kotlinmud.mob.race.impl.Elf
import kotlinmud.mob.race.impl.Faerie
import kotlinmud.mob.race.impl.Felid
import kotlinmud.mob.race.impl.Goblin
import kotlinmud.mob.race.impl.Ogre
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.Gender
import kotlinmud.mob.type.JobType
import kotlinmud.mob.type.SpecializationType
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
            AttributesBuilder()
                .hp(1)
                .mana(1)
                .mv(1)
                .strength(1)
                .intelligence(1)
                .wisdom(1)
                .dexterity(1)
                .constitution(1)
                .hit(1)
                .dam(1)
                .acBash(1)
                .acSlash(1)
                .acPierce(1)
                .acMagic(1)
                .build()
        )
        mob.equipped.add(item.build())

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
        val mob2 = testService.withMob {
            it.race(Elf())
        }

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
        val mob2 = testService.withMob {
            it.affects(affects(AffectType.CURSE))
        }

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
        val mob1 = testService.withMob { it.level(50) }
        val prob = ProbabilityTest()

        // given
        val mob2 = testService.withMob {
            it.level(50)
                .affects(affects(AffectType.BERSERK))
        }

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
        val mob1 = testService.withMob {
            it.race(Faerie())
        }
        val mob2 = testService.withMob {
            it.race(Ogre())
        }

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
        val mob2 = testService.withMob {
            it.disposition(Disposition.FIGHTING)
        }

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
        val mob1 = testService.withMob { it.race(Goblin()) }
        val mob2 = testService.withMob { it.race(Ogre()) }

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
        val mob1 = testService.withMob { it.specialization(SpecializationType.MAGE) }
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
        val mob1 = testService.withMob { it.specialization(SpecializationType.CLERIC) }
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
        mob.equipped.add(
            testService.buildItem(
                testService.itemBuilder()
                    .position(Position.SHIELD)
                    .attributes(
                        AttributesBuilder()
                            .hp(100)
                            .build()
                    ),
                mob
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
        val mob1 = testService.withMob {
            it.gold(5)
                .hp(1)
        }
        val mob2 = testService.withMob {
            it.gold(5)
                .hp(1)
        }
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

        // given
        test.respawnWorld()

        // when
        val mob = test.getMobRooms().find { it.mob.id == 1 }!!.mob

        // then
        assertThat(mob.affects().getAffects()).hasSize(2)
    }

    @Test
    fun testCreateCorpseTransfersInventoryAndEquipment() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createMob()
        test.createItem(mob)
        mob.equipped.add(test.itemBuilder().build())
        val inventoryAmount = test.countItemsFor(mob)

        // when
        val corpse = test.createCorpseFrom(mob)

        // then
        assertThat(test.countItemsFor(corpse)).isEqualTo(inventoryAmount)
        assertThat(mob.equipped).hasSize(0)
        assertThat(test.countItemsFor(mob)).isEqualTo(0)
    }

    @Test
    fun testCanMapAndLoadMob() {
        // setup
        val test = createTestService()

        // given
        val mob = test.createPlayerMobBuilder()
            .description("a mob of great refinement is here")
            .brief("a mob of great refinement")
            .disposition(Disposition.SITTING)
            .gender(Gender.ANY)
            .gold(2)
            .goldMin(1)
            .goldMax(3)
            .hp(100)
            .mana(101)
            .mv(102)
            .isNpc(false)
            .race(Felid())
            .job(JobType.SCAVENGER)
            .specialization(SpecializationType.MAGE)
            .id(1)
            .level(8)
            .savingThrows(13)
            .wimpy(14)
            .maxItems(21)
            .maxWeight(22)
            .skills(mutableMapOf(Pair(SkillType.ALCHEMY, 51)))
            .affects(mutableListOf(AffectInstance(AffectType.BERSERK, 12)))
            .attributes(AttributesBuilder()
                .strength(1)
                .intelligence(2)
                .wisdom(3)
                .dexterity(4)
                .constitution(5)
                .hit(6)
                .dam(7)
                .hp(8)
                .mana(9)
                .mv(10)
                .acBash(11)
                .acSlash(12)
                .acPierce(13)
                .acMagic(14)
                .build()
            )
            .build()

        // when
        val data = mapMob(mob)
        val model = MobLoader(Tokenizer(data), 10, false)
            .load()
            .build()

        // then
        val buf1 = StringBuilder()
        pp(mob, 2, buf1, 80)
        val buf2 = StringBuilder()
        pp(model, 2, buf2, 80)
        assertThat(buf2.toString()).isEqualTo(buf1.toString())
    }
}

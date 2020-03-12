package kotlinmud.mob

import assertk.assertThat
import assertk.assertions.isGreaterThan
import assertk.assertions.isLessThan
import kotlin.test.assertEquals
import kotlinmud.affect.AffectType
import kotlinmud.attributes.Attribute
import kotlinmud.attributes.Attributes
import kotlinmud.mob.fight.DamageType
import kotlinmud.mob.race.impl.Elf
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
        testService.createItem(mob.equipped, Attributes(
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1
        ))

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
        var mob1Successes = 0
        var mob2Successes = 0
        var iterations = 1000

        // given
        val mob2 = testService.buildMob(
            testService.mobBuilder().setRace(Elf())
        )

        // when
        while (iterations > 0) {
            if (mob1.savesAgainst(DamageType.NONE)) {
                mob1Successes++
            }
            if (mob2.savesAgainst(DamageType.NONE)) {
                mob2Successes++
            }
            iterations--
        }

        // then
        assertThat(mob1Successes).isLessThan(mob2Successes)
    }

    @Test
    fun testCurseSavePenalty() {
        // setup
        val testService = createTestService()
        val mob1 = testService.createMob()
        var mob1Successes = 0
        var mob2Successes = 0
        var iterations = 1000

        // given
        val mob2 = testService.buildMob(testService.mobBuilder().addAffect(AffectType.CURSE))

        // when
        while (iterations > 0) {
            if (mob1.savesAgainst(DamageType.NONE)) {
                mob1Successes++
            }
            if (mob2.savesAgainst(DamageType.NONE)) {
                mob2Successes++
            }
            iterations--
        }

        // then
        assertThat(mob1Successes).isGreaterThan(mob2Successes)
    }
}

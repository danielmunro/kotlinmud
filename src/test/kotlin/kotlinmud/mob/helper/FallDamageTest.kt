package kotlinmud.mob.helper

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.mob.constant.FALL_DAMAGE_EXTREME_MULTIPLIER
import kotlinmud.mob.constant.FALL_DAMAGE_HIGH
import kotlinmud.mob.constant.FALL_DAMAGE_LOW
import kotlinmud.mob.constant.FALL_DAMAGE_MEDIUM
import kotlinmud.mob.constant.HEIGHT_DIFFERENCE_HIGH
import kotlinmud.mob.constant.HEIGHT_DIFFERENCE_LOW
import kotlinmud.mob.constant.HEIGHT_DIFFERENCE_MEDIUM
import kotlinmud.test.createTestService
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class FallDamageTest {
    @Test
    fun testFallSmallHeight() {
        // setup
        val test = createTestService()
        val mob = test.createMob()
        val dst = test.createRoom {
            it.elevation = 0
        }
        val src = test.getStartRoom {
            it.north = dst
        }
        val hp = transaction { mob.hp }

        // given
        transaction { src.elevation = HEIGHT_DIFFERENCE_LOW + 1 }

        // when
        test.runAction("n")

        // then
        assertThat(mob.hp).isEqualTo(hp - FALL_DAMAGE_LOW)
    }

    @Test
    fun testFallMediumHeight() {
        // setup
        val test = createTestService()
        val mob = test.createMob()
        val dst = test.createRoom {
            it.elevation = 0
        }
        val src = test.getStartRoom {
            it.north = dst
        }
        val hp = transaction { mob.hp }

        // given
        transaction { src.elevation = HEIGHT_DIFFERENCE_MEDIUM + 1 }

        // when
        test.runAction("n")

        // then
        assertThat(mob.hp).isEqualTo(hp - FALL_DAMAGE_MEDIUM)
    }

    @Test
    fun testFallHighHeight() {
        // setup
        val test = createTestService()
        val mob = test.createMob()
        val dst = test.createRoom {
            it.elevation = 0
        }
        val src = test.getStartRoom {
            it.north = dst
        }
        val hp = transaction { mob.hp }

        // given
        transaction { src.elevation = HEIGHT_DIFFERENCE_HIGH + 1 }

        // when
        test.runAction("n")

        // then
        assertThat(mob.hp).isEqualTo(hp - FALL_DAMAGE_HIGH)
    }

    @Test
    fun testFallExtremeHeight() {
        // setup
        val test = createTestService()
        val mob = test.createMob()
        val dst = test.createRoom {
            it.elevation = 0
        }
        val src = test.getStartRoom {
            it.north = dst
        }
        val hp = transaction { mob.hp }
        val difference = HEIGHT_DIFFERENCE_HIGH + 10

        // given
        transaction { src.elevation = difference }

        // when
        test.runAction("n")

        // then
        assertThat(mob.hp).isEqualTo(hp - (FALL_DAMAGE_EXTREME_MULTIPLIER * difference))
    }
}

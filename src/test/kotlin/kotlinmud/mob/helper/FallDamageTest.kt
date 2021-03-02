package kotlinmud.mob.helper

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isLessThan
import kotlinmud.attributes.type.Attribute
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
        val dst = test.createRoom()
        val src = test.createRoomBuilder()
                .elevation(1 + HEIGHT_DIFFERENCE_LOW)
                .north(dst)
                .build()

        // given
        mob.room = src

        // when
        test.runAction("n")

        // then
        assertThat(mob.hp).isEqualTo(mob.calc(Attribute.HP) - FALL_DAMAGE_LOW)
    }

    @Test
    fun testFallMediumHeight() {
        // setup
        val test = createTestService()
        val mob = test.createMob()
        val dst = test.createRoom()
        val src = test.createRoomBuilder()
                .elevation(HEIGHT_DIFFERENCE_MEDIUM + 1)
                .north(dst)
                .build()

        // given
        mob.room = src

        // when
        test.runAction("n")

        // then
        assertThat(mob.hp).isEqualTo(mob.calc(Attribute.HP) - FALL_DAMAGE_MEDIUM)
    }

    @Test
    fun testFallHighHeight() {
        // setup
        val test = createTestService()
        val mob = test.createMob()
        val dst = test.createRoom()
        val src = test.createRoomBuilder()
                .north(dst)
                .elevation(HEIGHT_DIFFERENCE_HIGH + 1)
                .build()

        // given
        mob.room = src

        // when
        test.runAction("n")

        // then
        assertThat(mob.hp).isEqualTo(mob.calc(Attribute.HP) - FALL_DAMAGE_HIGH)
    }

    @Test
    fun testFallExtremeHeight() {
        // setup
        val test = createTestService()
        val mob = test.createMob()
        val dst = test.createRoom()
        val src = test.createRoomBuilder()
                .elevation(HEIGHT_DIFFERENCE_HIGH + 10)
                .north(dst)
                .build()

        // given
        mob.room = src

        // when
        test.runAction("n")

        // then
        assertThat(mob.hp).isLessThan(mob.calc(Attribute.HP))
    }
}

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
import kotlinmud.room.model.Room
import kotlinmud.room.type.Area
import kotlinmud.test.helper.createTestService
import kotlinmud.test.service.TestService
import org.junit.Test

class FallDamageTest {
    @Test
    fun testFallSmallHeight() {
        // setup
        val test = createTestService()
        val mob = test.createMob()

        // given
        mob.room = getRoom(test, HEIGHT_DIFFERENCE_LOW + 1)

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

        // given
        mob.room = getRoom(test, HEIGHT_DIFFERENCE_MEDIUM + 1)

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

        // given
        mob.room = getRoom(test, HEIGHT_DIFFERENCE_HIGH + 1)

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

        // given
        mob.room = getRoom(test, HEIGHT_DIFFERENCE_HIGH + 10)

        // when
        test.runAction("n")

        // then
        assertThat(mob.hp).isLessThan(mob.calc(Attribute.HP))
    }

    private fun getRoom(test: TestService, height: Int): Room {
        return test.createRoomBuilder()
            .elevation(height)
            .north(test.getStartRoom())
            .name("foo")
            .description("bar")
            .area(Area.Test)
            .build()
    }
}

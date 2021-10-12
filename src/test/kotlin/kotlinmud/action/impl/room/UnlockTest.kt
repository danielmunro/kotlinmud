package kotlinmud.action.impl.room

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.item.type.ItemType
import kotlinmud.mob.model.Mob
import kotlinmud.room.model.Door
import kotlinmud.room.type.DoorDisposition
import kotlinmud.test.helper.createTestService
import kotlinmud.test.service.TestService
import org.junit.Test

class UnlockTest {
    private val keyId = 1

    private fun setup(test: TestService): Door {
        val start = test.getStartRoom()
        val destination = test.createRoom()
        start.west = destination
        start.westDoor = Door(
            "a door",
            "tbd",
            "tbd",
            DoorDisposition.LOCKED,
            keyId,
        )
        return start.westDoor!!
    }

    private fun createMobWithKey(test: TestService): Mob {
        return test.createPlayerMob {
            it.items.add(
                test.createItemBuilder().also { builder ->
                    builder.type = ItemType.KEY
                    builder.keyId = keyId
                }.build()
            )
        }
    }

    @Test
    fun testMobCanUnlockDoorSuccessfully() {
        // setup
        val test = createTestService()
        setup(test)

        // given
        val mob = createMobWithKey(test)

        // when
        val response = test.runAction("unlock door")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you unlock a door.")
        assertThat(response.message.toObservers).isEqualTo("$mob unlocks a door.")
    }

    @Test
    fun testCannotUnlockOpenDoor() {
        // setup
        val test = createTestService()
        val door = setup(test)
        createMobWithKey(test)

        // given
        door.disposition = DoorDisposition.OPEN

        // when
        val response = test.runAction("unlock door")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("the door is not locked.")
    }

    @Test
    fun testCannotUnlockClosedDoor() {
        // setup
        val test = createTestService()
        val door = setup(test)
        createMobWithKey(test)

        // given
        door.disposition = DoorDisposition.CLOSED

        // when
        val response = test.runAction("unlock door")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("the door is not locked.")
    }

    @Test
    fun testUnlockRequiresAKey() {
        // setup
        val test = createTestService()
        setup(test)

        // when
        val response = test.runAction("unlock door")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you lack the key.")
    }
}

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
import java.util.UUID

class LockTest {
    private val uuid = UUID.randomUUID()

    private fun setup(test: TestService): Door {
        val start = test.getStartRoom()
        val destination = test.createRoom()
        start.west = destination
        start.westDoor = Door(
            "a door",
            "tbd",
            DoorDisposition.CLOSED,
            uuid,
        )
        return start.westDoor!!
    }

    private fun createMobWithKey(test: TestService): Mob {
        return test.createPlayerMob {
            it.items.add(
                test.createItemBuilder().also { builder ->
                    builder.type = ItemType.KEY
                    builder.canonicalId = uuid
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
        val response = test.runAction("lock door")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you lock a door.")
        assertThat(response.message.toObservers).isEqualTo("$mob locks a door.")
    }

    @Test
    fun testLockRequiresClosedDoor() {
        // setup
        val test = createTestService()
        val door = setup(test)
        createMobWithKey(test)

        // given
        door.disposition = DoorDisposition.OPEN

        // when
        val response = test.runAction("lock door")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you need to close the door to lock it.")
    }

    @Test
    fun testUnlockRequiresAKey() {
        // setup
        val test = createTestService()
        setup(test)

        // when
        val response = test.runAction("lock door")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you lack the key.")
    }
}

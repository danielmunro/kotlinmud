package kotlinmud.world.service

import assertk.assertThat
import assertk.assertions.isNotNull
import kotlinmud.room.model.Door
import kotlinmud.room.type.Direction
import kotlinmud.room.type.DoorDisposition
import kotlinmud.test.helper.createTestService
import org.junit.Test

class AreaBuilderServiceTest {
    companion object {
        fun createDoorModel(): Door {
            return Door("a door", "tbd", "tbd", DoorDisposition.CLOSED, 1)
        }
    }

    @Test
    fun testDoorBuildingIsReciprocalFromNorth() {
        // setup
        val test = createTestService()
        val svc = test.createAreaBuilderService()

        // given
        svc.buildRoom("start")
            .buildRoom("end", Direction.NORTH)

        // when
        svc.buildDoor(Direction.SOUTH, createDoorModel(),)

        // then
        assertThat(svc.getRoomFromLabel("start").northDoor).isNotNull()
        assertThat(svc.getRoomFromLabel("end").southDoor).isNotNull()
    }

    @Test
    fun testDoorBuildingIsReciprocalFromSouth() {
        // setup
        val test = createTestService()
        val svc = test.createAreaBuilderService()

        // given
        svc.buildRoom("start")
            .buildRoom("end", Direction.SOUTH)

        // when
        svc.buildDoor(Direction.NORTH, createDoorModel())

        // then
        assertThat(svc.getRoomFromLabel("start").southDoor).isNotNull()
        assertThat(svc.getRoomFromLabel("end").northDoor).isNotNull()
    }

    @Test
    fun testDoorBuildingIsReciprocalFromEast() {
        // setup
        val test = createTestService()
        val svc = test.createAreaBuilderService()

        // given
        svc.buildRoom("start")
            .buildRoom("end", Direction.EAST)

        // when
        svc.buildDoor(Direction.WEST, createDoorModel())

        // then
        assertThat(svc.getRoomFromLabel("start").eastDoor).isNotNull()
        assertThat(svc.getRoomFromLabel("end").westDoor).isNotNull()
    }

    @Test
    fun testDoorBuildingIsReciprocalFromWest() {
        // setup
        val test = createTestService()
        val svc = test.createAreaBuilderService()

        // given
        svc.buildRoom("start")
            .buildRoom("end", Direction.WEST)

        // when
        svc.buildDoor(Direction.EAST, createDoorModel())

        // then
        assertThat(svc.getRoomFromLabel("start").westDoor).isNotNull()
        assertThat(svc.getRoomFromLabel("end").eastDoor).isNotNull()
    }

    @Test
    fun testDoorBuildingIsReciprocalFromUp() {
        // setup
        val test = createTestService()
        val svc = test.createAreaBuilderService()

        // given
        svc.buildRoom("start")
            .buildRoom("end", Direction.UP)

        // when
        svc.buildDoor(Direction.DOWN, createDoorModel())

        // then
        assertThat(svc.getRoomFromLabel("start").upDoor).isNotNull()
        assertThat(svc.getRoomFromLabel("end").downDoor).isNotNull()
    }

    @Test
    fun testDoorBuildingIsReciprocalFromDown() {
        // setup
        val test = createTestService()
        val svc = test.createAreaBuilderService()

        // given
        svc.buildRoom("start")
            .buildRoom("end", Direction.DOWN)

        // when
        svc.buildDoor(Direction.UP, createDoorModel())

        // then
        assertThat(svc.getRoomFromLabel("start").downDoor).isNotNull()
        assertThat(svc.getRoomFromLabel("end").upDoor).isNotNull()
    }
}

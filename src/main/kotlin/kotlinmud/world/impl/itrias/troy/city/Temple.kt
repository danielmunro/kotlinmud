package kotlinmud.world.impl.itrias.troy.city

import kotlinmud.mob.race.impl.Human
import kotlinmud.room.model.Door
import kotlinmud.room.model.Room
import kotlinmud.room.type.Direction
import kotlinmud.room.type.DoorDisposition
import kotlinmud.world.service.AreaBuilderService

fun createTemple(areaBuilderService: AreaBuilderService, connection: Room) {
    areaBuilderService.startWith(connection)
        .buildRoom("temple", Direction.NORTH) {
            it.name = "The Temple of Matook"
            it.description = "Archway of the Temple of Matook."
        }

    val residence = areaBuilderService.startWith("temple")
        .buildRoom(Direction.NORTH)
        .buildDoor(
            Direction.SOUTH,
            Door(
                "a sturdy oak door",
                "tbd",
                "tbd",
                DoorDisposition.CLOSED,
                2,
            )
        )
        .buildRoom("atrium", Direction.NORTH) {
            it.name = "Temple Atrium"
        }
        .buildRoom(Direction.WEST) {
            it.name = "Temple Prayer Rooms"
        }
        .startWith("atrium")
        .buildRoom(Direction.EAST) {
            it.name = "Temple Study"
        }
        .startWith("atrium")
        .buildRoom(Direction.NORTH) {
            it.name = "Grand Residence of The Prophet of Matook"
        }.lastRoom

    areaBuilderService.buildFodder(
        "a temple acolyte",
        "a temple acolyte is here, tending to the temple",
        "tbd",
        Human(),
        6,
        2,
    )

    areaBuilderService.buildFodder(
        "the Matook healer",
        "the healer of Matook is here, honing her craft",
        "tbd",
        Human(),
        9,
        1,
    ).also {
        it.randomizeRoom = false
        it.room = residence
    }
}

package kotlinmud.world.impl.itrias.lorimir

import kotlinmud.room.model.Room
import kotlinmud.room.type.Direction
import kotlinmud.world.service.AreaBuilderService

fun createLorimirForestLake(areaBuilderService: AreaBuilderService, connection: Room) {
    areaBuilderService.roomBuilder(
        "A path in the woods",
        "foo",
    )

    areaBuilderService
        .startWith(connection)
        .buildRoom(Direction.EAST) {
            it.name = "A path in the woods"
            it.description = "foo"
        }
        .buildRoom(Direction.DOWN)
        .buildRoom(Direction.EAST)
        .buildRoom(Direction.SOUTH) {
            it.name = "The trail begins to open up"
            it.description = "You can see the trail leading to an opening. A lake is in the distance."
        }
        .buildRoom(Direction.SOUTH) {
            it.name = "Lake at Lorimir Forest"
            it.description = "foo"
        }
}

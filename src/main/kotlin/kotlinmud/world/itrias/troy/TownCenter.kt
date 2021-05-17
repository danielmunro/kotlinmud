package kotlinmud.world.itrias.troy

import kotlinmud.room.builder.RoomBuilder
import kotlinmud.room.builder.build
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction

fun createTroyTownCenter(roomService: RoomService, connection: Room): Room {
    val roomBuilder = RoomBuilder(roomService).also {
        it.area = Area.Troy
        it.name = "The City of Troy"
    }

    val room1 = build(roomBuilder.also {
        it.name = "Main Street"
        it.description = "A well-worn cobblestone path connects the town center with the promenade. Shops line the bustling road."
    })
    val room2 = build(roomBuilder)
    val room3 = build(roomBuilder)
    val room4 = build(roomBuilder.also {
        it.name = "A Large Fountain"
        it.description = "The center of Troy is home to a large and ornate fountain. Pristine marble wraps around the fountain, leaving a dramatic glow in the sunlight."
    })

    connect(connection).to(room1)
        .to(room2, Direction.NORTH)
        .to(room3, Direction.NORTH)
        .to(room4, Direction.NORTH)

    return room4
}

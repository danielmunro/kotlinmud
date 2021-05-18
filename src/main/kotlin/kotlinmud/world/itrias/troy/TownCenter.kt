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
        it.description = "tbd"
    }

    val mainStreetBuilder = roomBuilder.copy().also {
        it.name = "Main Street"
        it.description = "A well-worn cobblestone path connects the town center with the promenade. Shops line the bustling road."
    }

    val walledRoad = roomBuilder.copy().also {
        it.name = "Walled Road"
    }

    val fountainRoom = build(
        roomBuilder.copy().also {
            it.name = "A Large Fountain"
            it.description = "The center of Troy is home to a large and ornate fountain. Pristine marble wraps around the fountain, leaving a dramatic glow in the sunlight."
        }
    )

    val northGate = build(
        roomBuilder.copy().also {
            it.name = "Troy North Gate"
            it.description = "tbd"
        }
    )

    val westGate = build(
        roomBuilder.copy().also {
            it.name = "Troy West Gate"
            it.description = "tbd"
        }
    )

    val eastGate = build(
        roomBuilder.copy().also {
            it.name = "Troy East Gate"
            it.description = "tbd"
        }
    )

    connect(connection)
        .toRoom(build(mainStreetBuilder))
        .toRoom(build(mainStreetBuilder), Direction.NORTH)
        .toRoom(build(mainStreetBuilder), Direction.NORTH)
        .toRoom(fountainRoom, Direction.NORTH)
        .toRoom(build(mainStreetBuilder), Direction.WEST)
        .toRoom(build(mainStreetBuilder), Direction.WEST)
        .toRoom(build(mainStreetBuilder), Direction.WEST)
        .toRoom(westGate)

    connect(fountainRoom)
        .toRoom(build(mainStreetBuilder), Direction.EAST)
        .toRoom(build(mainStreetBuilder), Direction.EAST)
        .toRoom(build(mainStreetBuilder), Direction.EAST)
        .toRoom(eastGate)

    connect(fountainRoom)
        .toRoom(build(mainStreetBuilder), Direction.NORTH)
        .toRoom(build(mainStreetBuilder), Direction.NORTH)
        .toRoom(build(mainStreetBuilder), Direction.NORTH)
        .toRoom(northGate, Direction.NORTH)

    connect(westGate)
        .toRoom(build(walledRoad), Direction.NORTH)
        .toRoom(build(walledRoad), Direction.NORTH)
        .toRoom(build(walledRoad), Direction.NORTH)
        .toRoom(build(walledRoad), Direction.EAST)
        .toRoom(build(walledRoad), Direction.EAST)
        .toRoom(build(walledRoad), Direction.EAST)
        .toRoom(northGate, Direction.EAST)
        .toRoom(build(walledRoad), Direction.EAST)
        .toRoom(build(walledRoad), Direction.EAST)
        .toRoom(build(walledRoad), Direction.EAST)
        .toRoom(build(walledRoad), Direction.SOUTH)
        .toRoom(build(walledRoad), Direction.SOUTH)
        .toRoom(build(walledRoad), Direction.SOUTH)
        .toRoom(eastGate, Direction.SOUTH)

    return northGate
}

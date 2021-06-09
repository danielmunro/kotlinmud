package kotlinmud.world.impl.itrias.troy

import kotlinmud.item.type.Drink
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.mob.race.impl.Canid
import kotlinmud.mob.race.impl.Human
import kotlinmud.room.builder.build
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.type.Direction
import kotlinmud.type.RoomCanonicalId
import kotlinmud.world.service.AreaBuilderService

fun createTroyTownCenter(
    areaBuilderService: AreaBuilderService,
    connection: Room
) {
    val roomBuilder = areaBuilderService.roomBuilder(
        "Main Street",
        "A well-worn cobblestone path connects the town center with the promenade. Shops line the bustling road.",
    )

    val fountainRoom = areaBuilderService.buildRoom("fountain") {
        it.name = "A Large Fountain"
        it.description = "The center of Troy is home to a large and ornate fountain. Pristine marble wraps around the fountain, leaving a dramatic glow in the sunlight."
        it.canonicalId = RoomCanonicalId.START_ROOM
        it.items = mutableListOf(
            areaBuilderService.itemBuilder(
                "an ornate marble fountain",
                "tbd"
            ).also { item ->
                item.canOwn = false
                item.type = ItemType.DRINK
                item.material = Material.MINERAL
                item.drink = Drink.WATER
            }.build()
        )
    }.getLastRoom()

    val walledRoad = roomBuilder.copy {
        it.name = "Walled Road"
    }

    val northGate = areaBuilderService.buildRoom {
        it.name = "Troy North Gate"
        it.description = "tbd"
    }.getLastRoom()

    val westGate = areaBuilderService.buildRoom {
        it.name = "Troy West Gate"
        it.description = "tbd"
    }.getLastRoom()

    val eastGate = areaBuilderService.buildRoom {
        it.name = "Troy East Gate"
        it.description = "tbd"
    }.getLastRoom()

    areaBuilderService.buildFodder(
        "a wandering vagabond",
        "a vagabond is wandering around",
        "tbd",
        Human(),
        4,
        10,
    )

    areaBuilderService.buildFodder(
        "a wandering trader",
        "a wandering trader is here, looking for their next deal",
        "tbd",
        Human(),
        6,
        3,
    )

    areaBuilderService.buildFodder(
        "a beastly fido",
        "a beastly fido is here, looking for scraps",
        "tbd",
        Canid(),
        2,
        8,
    )

    areaBuilderService.buildScavenger(
        "a janitor",
        "a janitor is here, sweeping up",
        "tbd",
        Human(),
        4,
        2,
    )

    areaBuilderService.buildFodder(
        "the mayor of Troy",
        "the mayor of Troy is here, garnering support for his next campaign",
        "tbd",
        Human(),
        10,
        1,
    )

    val southGate = createTroySouthGate(areaBuilderService, connection)
    val westRoad = createTroyWestSide(areaBuilderService, fountainRoom)
//    val northRoad = createTroyNorthSide(mobService, roomService, itemService, fountainRoom)
//    val eastRoad = createTroyEastSide(mobService, roomService, fountainRoom)

//    connect(northGate).toRoom(northRoad, Direction.SOUTH)
//    connect(westGate).toRoom(westRoad, Direction.EAST)
//    connect(eastGate).toRoom(eastRoad, Direction.WEST)

    connect(southGate)
        .toRoom(fountainRoom, Direction.NORTH)

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
}

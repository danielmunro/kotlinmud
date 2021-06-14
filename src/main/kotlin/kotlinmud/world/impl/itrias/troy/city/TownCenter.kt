package kotlinmud.world.impl.itrias.troy.city

import kotlinmud.item.type.Drink
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.mob.race.impl.Canid
import kotlinmud.mob.race.impl.Human
import kotlinmud.room.builder.build
import kotlinmud.room.model.Room
import kotlinmud.room.type.Direction
import kotlinmud.type.RoomCanonicalId
import kotlinmud.world.service.AreaBuilderService

fun createTroyTownCenter(
    areaBuilderService: AreaBuilderService,
    connection: Room
) {
    val fountainRoom = areaBuilderService.buildRoom("fountain") {
        it.name = "A Large Fountain"
        it.description = "The center of Troy is home to a large and ornate fountain. Pristine marble wraps around the fountain, leaving a dramatic glow in the sunlight."
        it.canonicalId = RoomCanonicalId.START_ROOM
        it.items = mutableListOf(
            areaBuilderService.itemBuilder(
                "an ornate marble fountain",
                "tbd",
            ).also { item ->
                item.canOwn = false
                item.type = ItemType.DRINK
                item.material = Material.MINERAL
                item.drink = Drink.WATER
            }.build()
        )
    }.lastRoom

    areaBuilderService.buildRoom("north gate") {
        it.name = "Troy North Gate"
        it.description = "tbd"
    }

    areaBuilderService.buildRoom("west gate") {
        it.name = "Troy West Gate"
        it.description = "tbd"
    }

    areaBuilderService.buildRoom("east gate") {
        it.name = "Troy East Gate"
        it.description = "tbd"
    }

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

    createTroySouthGate(
        areaBuilderService,
        fountainRoom,
        connection,
    )

    createTroyWestSide(
        areaBuilderService,
        fountainRoom,
    )

    createTroyNorthSide(
        areaBuilderService,
        fountainRoom,
    )

    createTroyEastSide(
        areaBuilderService,
        fountainRoom,
    )

    areaBuilderService.startWith("west gate")
        .buildRoom(Direction.NORTH) {
            it.name = "Walled road"
        }
        .buildRoom(Direction.NORTH)
        .buildRoom(Direction.NORTH)
        .buildRoom(Direction.EAST)
        .buildRoom(Direction.EAST)
        .connectTo("north gate", Direction.EAST)
        .buildRoom(Direction.EAST)
        .buildRoom(Direction.EAST)
        .buildRoom(Direction.EAST)
        .buildRoom(Direction.SOUTH)
        .buildRoom(Direction.SOUTH)
        .connectTo("east gate", Direction.SOUTH)
}

package kotlinmud.world.impl.itrias.troy.city

import kotlinmud.item.type.Drink
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.mob.race.impl.Canid
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.type.JobType
import kotlinmud.room.model.Room
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction
import kotlinmud.type.RoomCanonicalId
import kotlinmud.world.service.AreaBuilderService

fun createTroyTownCenter(
    areaBuilderService: AreaBuilderService,
    connection: Room
): Map<Area, Room> {
    val fountainRoom = areaBuilderService.buildRoom("fountain") {
        it.name = "A Large Fountain"
        it.description = "The center of Troy is home to a large and ornate fountain. Pristine marble wraps around the fountain, leaving a dramatic glow in the sunlight."
        it.canonicalId = RoomCanonicalId.StartRoom
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

    areaBuilderService.buildFodder(
        "a Troy city guard",
        "a guard of the city is here, patrolling the streets",
        "tbd",
        Human(),
        10,
        1,
    ).also {
        it.job = JobType.PATROL
        it.randomizeRoom = false
        it.room = fountainRoom
        it.messages = listOf("move along, citizen")
    }

    val guardBuilder = {
        areaBuilderService.buildFodder(
            "a Troy city guard",
            "an imposing Troy city guard is here, ensuring safety at the gate",
            "tbd",
            Human(),
            10,
            2,
        ).also {
            it.job = JobType.GUARD
            it.randomizeRoom = false
            it.room = areaBuilderService.lastRoom
        }
    }

    areaBuilderService.buildRoom("north gate") {
        it.name = "Troy North Gate"
        it.description = "tbd"
    }
    guardBuilder()

    areaBuilderService.buildRoom("west gate") {
        it.name = "Troy West Gate"
        it.description = "tbd"
    }
    guardBuilder()

    areaBuilderService.buildRoom("east gate") {
        it.name = "Troy East Gate"
        it.description = "tbd"
    }
    guardBuilder()

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
    ).also {
        it.messages = listOf(
            "Welcome to the great city of Troy!",
            "Remember to vote for yours truly in the next election.",
        )
        it.randomizeRoom = false
        it.room = fountainRoom
    }

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

    return mapOf(
        Pair(Area.IronBluffMainRoad, areaBuilderService.getRoomFromLabel("west gate")),
    )
}

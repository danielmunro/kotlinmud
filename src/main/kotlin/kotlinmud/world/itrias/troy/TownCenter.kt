package kotlinmud.world.itrias.troy

import kotlinmud.mob.race.impl.Canid
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.JobType
import kotlinmud.mob.type.MobCanonicalId
import kotlinmud.respawn.helper.respawn
import kotlinmud.respawn.model.MobRespawn
import kotlinmud.room.builder.RoomBuilder
import kotlinmud.room.builder.build
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction

fun createTroyTownCenter(mobService: MobService, roomService: RoomService, connection: Room) {
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

    val outsideWall = roomBuilder.copy().also {
        it.name = "Outside the gates of Troy"
        it.description = "tbd"
    }

    respawn(
        MobRespawn(
            mobService.builder().also {
                it.canonicalId = MobCanonicalId.WanderingVagabond
                it.name = "a vagabond is wandering around"
                it.brief = "a wandering vagabond"
                it.description = "tbd"
            },
            Area.Troy,
            10
        )
    )

    respawn(
        MobRespawn(
            mobService.builder().also {
                it.canonicalId = MobCanonicalId.BeastlyFido
                it.name = "a beastly fido is here, looking for scraps"
                it.brief = "a beastly fido"
                it.description = "tbd"
                it.race = Canid()
            },
            Area.Troy,
            10
        )
    )

    respawn(
        MobRespawn(
            mobService.builder().also {
                it.canonicalId = MobCanonicalId.Janitor
                it.name = "a janitor is here, sweeping up"
                it.brief = "a janitor"
                it.description = "tbd"
                it.job = JobType.SCAVENGER
            },
            Area.Troy,
            2
        )
    )

    respawn(
        MobRespawn(
            mobService.builder().also {
                it.canonicalId = MobCanonicalId.MayorOfTroy
                it.name = "the mayor of Troy is here, garnering support for his next campaign"
                it.brief = "the mayor of Troy"
                it.description = "tbd"
            },
            Area.Troy,
            1
        )
    )

    connect(connection)
        .toRoom(build(mainStreetBuilder))
        .toRoom(build(mainStreetBuilder), Direction.NORTH)
        .toRoom(build(mainStreetBuilder), Direction.NORTH)
        .toRoom(fountainRoom, Direction.NORTH)
        .toRoom(build(mainStreetBuilder), Direction.WEST)
        .toRoom(build(mainStreetBuilder), Direction.WEST)
        .toRoom(build(mainStreetBuilder), Direction.WEST)
        .toRoom(westGate, Direction.WEST)
        .toRoom(build(outsideWall), Direction.WEST)

    connect(fountainRoom)
        .toRoom(build(mainStreetBuilder), Direction.EAST)
        .toRoom(build(mainStreetBuilder), Direction.EAST)
        .toRoom(build(mainStreetBuilder), Direction.EAST)
        .toRoom(eastGate, Direction.EAST)
        .toRoom(build(outsideWall), Direction.EAST)

    connect(fountainRoom)
        .toRoom(build(mainStreetBuilder), Direction.NORTH)
        .toRoom(build(mainStreetBuilder), Direction.NORTH)
        .toRoom(build(mainStreetBuilder), Direction.NORTH)
        .toRoom(northGate, Direction.NORTH)
        .toRoom(build(outsideWall), Direction.NORTH)

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

package kotlinmud.world.impl.itrias.troy

import kotlinmud.item.service.ItemService
import kotlinmud.item.type.Drink
import kotlinmud.item.type.ItemCanonicalId
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.mob.race.impl.Canid
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.service.MobService
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.JobType
import kotlinmud.mob.type.MobCanonicalId
import kotlinmud.respawn.helper.itemRespawnsFor
import kotlinmud.respawn.helper.mobRespawnsFor
import kotlinmud.respawn.helper.respawn
import kotlinmud.respawn.model.ItemMobRespawn
import kotlinmud.respawn.model.MobRespawn
import kotlinmud.room.builder.build
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction
import kotlinmud.world.factory.createAmberAle
import kotlinmud.world.factory.createCureBlindnessPotion
import kotlinmud.world.factory.createCureLightPotion
import kotlinmud.world.factory.createCurePoisonPotion
import kotlinmud.world.factory.createHastePotion
import kotlinmud.world.factory.createIPA
import kotlinmud.world.factory.createPorter
import kotlinmud.world.factory.createRemoveCursePotion

fun createTroyTownCenter(mobService: MobService, roomService: RoomService, itemService: ItemService, connection: Room) {
    val roomBuilder = roomService.builder(
        "The City of Troy",
        "tbd",
        Area.Troy,
    )

    val mainStreetBuilder = roomBuilder.copy {
        it.name = "Main Street"
        it.description = "A well-worn cobblestone path connects the town center with the promenade. Shops line the bustling road."
    }

    val walledRoad = roomBuilder.copy {
        it.name = "Walled Road"
    }

    val fountainRoom = build(
        roomBuilder.copy {
            it.name = "A Large Fountain"
            it.description = "The center of Troy is home to a large and ornate fountain. Pristine marble wraps around the fountain, leaving a dramatic glow in the sunlight."
        }
    )

    fountainRoom.items.add(
        itemService.builder(
            "an ornate marble fountain",
            "tbd"
        ).also {
            it.canOwn = false
            it.type = ItemType.DRINK
            it.material = Material.MINERAL
            it.drink = Drink.WATER
        }.build()
    )

    val northGate = build(
        roomBuilder.copy {
            it.name = "Troy North Gate"
            it.description = "tbd"
        }
    )

    val westGate = build(
        roomBuilder.copy {
            it.name = "Troy West Gate"
            it.description = "tbd"
        }
    )

    val eastGate = build(
        roomBuilder.copy {
            it.name = "Troy East Gate"
            it.description = "tbd"
        }
    )

    val outsideWall = roomBuilder.copy {
        it.name = "Outside the gates of Troy"
        it.description = "tbd"
    }

    mobRespawnsFor(
        Area.Troy,
        listOf(
            Pair(
                mobService.builder(
                    "a wandering vagabond",
                    "a vagabond is wandering around",
                    "tbd",
                ).also {
                    it.canonicalId = MobCanonicalId.WanderingVagabond
                    it.job = JobType.FODDER
                    it.race = Human()
                },
                10,
            ),
            Pair(
                mobService.builder(
                    "a wandering trader",
                    "a wandering trader is here, looking for their next deal",
                    "tbd",
                ).also {
                    it.canonicalId = MobCanonicalId.WanderingTrader
                    it.job = JobType.FODDER
                    it.race = Human()
                },
                3,
            ),
            Pair(
                mobService.builder(
                    "a beastly fido",
                    "a beastly fido is here, looking for scraps",
                    "tbd",
                ).also {
                    it.canonicalId = MobCanonicalId.BeastlyFido
                    it.job = JobType.FODDER
                    it.race = Canid()
                },
                10,
            ),
            Pair(
                mobService.builder(
                    "a janitor",
                    "a janitor is here, sweeping up",
                    "tbd",
                ).also {
                    it.canonicalId = MobCanonicalId.Janitor
                    it.job = JobType.SCAVENGER
                    it.race = Human()
                },
                2,
            ),
            Pair(
                mobService.builder(
                    "the mayor of Troy",
                    "the mayor of Troy is here, garnering support for his next campaign",
                    "tbd",
                ).also {
                    it.canonicalId = MobCanonicalId.MayorOfTroy
                    it.race = Human()
                    it.job = JobType.FODDER
                },
                1,
            ),
        ),
    )

    val southGate = createTroySouthGate(mobService, roomService, itemService, connection)

    connect(southGate)
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

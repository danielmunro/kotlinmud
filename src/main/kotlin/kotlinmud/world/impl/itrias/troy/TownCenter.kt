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

    respawn(
        MobRespawn(
            mobService.builder(
                "a wandering vagabond",
                "a vagabond is wandering around",
                "tbd",
            ).also {
                it.canonicalId = MobCanonicalId.WanderingVagabond
                it.race = Human()
            },
            Area.Troy,
            10
        )
    )

    respawn(
        MobRespawn(
            mobService.builder(
                "a beastly fido",
                "a beastly fido is here, looking for scraps",
                "tbd",
            ).also {
                it.canonicalId = MobCanonicalId.BeastlyFido
                it.race = Canid()
            },
            Area.Troy,
            10
        )
    )

    respawn(
        MobRespawn(
            mobService.builder(
                "a janitor",
                "a janitor is here, sweeping up",
                "tbd",
            ).also {
                it.canonicalId = MobCanonicalId.Janitor
                it.job = JobType.SCAVENGER
                it.race = Human()
            },
            Area.Troy,
            2
        )
    )

    respawn(
        MobRespawn(
            mobService.builder(
                "the mayor of Troy",
                "the mayor of Troy is here, garnering support for his next campaign",
                "tbd",
            ).also {
                it.canonicalId = MobCanonicalId.MayorOfTroy
                it.race = Human()
            },
            Area.Troy,
            1
        )
    )

    val main1 = build(mainStreetBuilder)

    val potionShop = build(
        roomBuilder.copy {
            it.name = "Potions & Apothecary"
            it.description = "A potion shop."
        }
    )

    mobService.builder(
        "a potion brewer",
        "a potion brewer stands here",
        "tbd",
    ).also {
        it.room = potionShop
        it.canonicalId = MobCanonicalId.PotionBrewer
        it.job = JobType.SHOPKEEPER
    }.build()

    itemRespawnsFor(
        MobCanonicalId.PotionBrewer,
        listOf(
            Pair(createCureLightPotion(itemService), 100),
            Pair(createCurePoisonPotion(itemService), 100),
            Pair(createCureBlindnessPotion(itemService), 100),
            Pair(createRemoveCursePotion(itemService), 100),
            Pair(createHastePotion(itemService), 100),
        )
    )

    val tavern = build(
        roomBuilder.copy {
            it.name = "The Ramshackle Tavern"
            it.description = "A humble and aging wooden structure surrounds you. Patrons sit around dimly lit tables, swapping tales of yore."
        }
    )

    mobService.builder(
        "the barkeeper",
        "the barkeeper is here, cleaning out a mug",
        "tbd",
    ).also {
        it.room = tavern
        it.canonicalId = MobCanonicalId.Barkeeper
        it.job = JobType.SHOPKEEPER
    }

    itemRespawnsFor(
        MobCanonicalId.Barkeeper,
        listOf(
            Pair(createAmberAle(itemService), 100),
            Pair(createPorter(itemService), 100),
            Pair(createIPA(itemService), 100),
        )
    )

    connect(main1)
        .toRoom(listOf(
            Pair(potionShop, Direction.WEST),
            Pair(tavern, Direction.EAST),
        ))

    connect(connection)
        .toRoom(main1)
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

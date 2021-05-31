package kotlinmud.world.impl.itrias.lorimir

import kotlinmud.generator.service.SimpleMatrixService
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.Food
import kotlinmud.item.type.ItemCanonicalId
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.mob.race.impl.Canid
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.Gender
import kotlinmud.mob.type.JobType
import kotlinmud.mob.type.QuestGiver
import kotlinmud.respawn.helper.respawn
import kotlinmud.respawn.model.ItemAreaRespawn
import kotlinmud.respawn.model.MobRespawn
import kotlinmud.room.builder.build
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction
import kotlinmud.type.RoomCanonicalId

fun createLorimirForest(
    mobService: MobService,
    roomService: RoomService,
    itemService: ItemService,
    connection: Room
): Room {
    val builder = roomService.builder(
        "Deep in the heart of Lorimir Forest.",
        "tbd",
        Area.LorimirForest,
    )

    val intersection = build(builder)

    val massiveTreeBuilder = builder.copy {
        it.description = "Around a massive tree."
    }

    val deepBuilder = builder.copy {
        it.name = "A dark forest"
        it.description = "Deep in the heart of Lorimir Forest."
    }
    val captainRoom = build(
        deepBuilder.copy {
            it.canonicalId = RoomCanonicalId.PRAETORIAN_CAPTAIN_FOUND
        }
    )
    val matrix = SimpleMatrixService(builder).build(5, 5)

    connect(connection)
        .toRoom(build(builder), Direction.SOUTH)
        .toRoom(build(builder), Direction.SOUTH)
        .toRoom(
            listOf(
                Pair(intersection, Direction.WEST),
                Pair(build(builder), Direction.EAST)
            )
        )
    connect(intersection)
        .toRoom(build(builder), Direction.WEST)
        .toRoom(build(massiveTreeBuilder), Direction.WEST)
        .toRoom(build(massiveTreeBuilder), Direction.SOUTH)
        .toRoom(build(massiveTreeBuilder), Direction.WEST)
        .toRoom(captainRoom, Direction.NORTH)
        .toRoom(matrix[0][0], Direction.DOWN)

    respawn(
        ItemAreaRespawn(
            ItemCanonicalId.Mushroom,
            itemService.builder(
                "a small brown mushroom",
                "tbd",
                0.1,
            ).also {
                it.material = Material.ORGANIC
                it.food = Food.MUSHROOM
                it.canonicalId = ItemCanonicalId.Mushroom
                it.type = ItemType.FOOD
                it.worth = 0
            },
            Area.LorimirForest,
            10,
        ),
    )

    mobService.builder(
        "Captain Bartok",
        "an imposing figure stands here. Her armor bears the emblem of the Praetorian Guard",
        "Captain Bartok is here",
    ).also {
        it.gender = Gender.FEMALE
        it.room = captainRoom
        it.level = 10
        it.job = JobType.QUEST
        it.identifier = QuestGiver.PraetorianCaptainBartok
    }.build()

    mobService.buildFodder(
        "a small fox",
        "a small fox darts through the underbrush",
        "a small fox is here.",
        Canid(),
        3,
        Area.LorimirForest,
        10,
    )

    createGrongokHideout(mobService, roomService, itemService, matrix[0][4])

    return matrix[2][4]
}
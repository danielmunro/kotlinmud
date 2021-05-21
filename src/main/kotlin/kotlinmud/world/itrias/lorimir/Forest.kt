package kotlinmud.world.itrias.lorimir

import kotlinmud.generator.service.SimpleMatrixService
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.Food
import kotlinmud.item.type.ItemCanonicalId
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.mob.race.impl.Canid
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.Gender
import kotlinmud.mob.type.JobType
import kotlinmud.mob.type.MobCanonicalId
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
    val builder = roomService.builder().also {
        it.area = Area.LorimirForest
        it.name = "Deep in the heart of Lorimir Forest."
        it.description = "foo"
    }

    val intersection = build(builder)

    val massiveTreeBuilder = builder.copy().also {
        it.description = "Around a massive tree."
    }

    val deepBuilder = builder.copy().also {
        it.name = "A dark forest"
        it.description = "Deep in the heart of Lorimir Forest."
    }
    val captainRoom = build(
        deepBuilder.copy().also {
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
                "tbd"
            ).also {
                it.material = Material.ORGANIC
                it.food = Food.MUSHROOM
                it.canonicalId = ItemCanonicalId.Mushroom
                it.type = ItemType.FOOD
            },
            Area.LorimirForest,
            10,
        ),
    )

    mobService.builder().also {
        it.name = "Captain Bartok"
        it.brief = "an imposing figure stands here. Her armor bears the emblem of the Praetorian Guard"
        it.description = "Captain Bartok is here"
        it.gender = Gender.FEMALE
        it.room = captainRoom
        it.level = 10
        it.job = JobType.QUEST
        it.canonicalId = MobCanonicalId.PraetorianCaptainBartok
        it.race = Human()
    }.build()

    respawn(
        MobRespawn(
            mobService.builder().also {
                it.name = "a small fox"
                it.brief = "a small fox darts through the underbrush"
                it.description = "a small fox is here."
                it.level = 3
                it.race = Canid()
                it.canonicalId = MobCanonicalId.SmallFox
            },
            Area.LorimirForest,
            10
        )
    )

    createGrongokHideout(mobService, roomService, matrix[0][4])

    return matrix[2][4]
}

package kotlinmud.world.itrias.lorimir

import kotlinmud.generator.service.SimpleMatrixService
import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.model.ItemAreaRespawn
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.Food
import kotlinmud.item.type.ItemCanonicalId
import kotlinmud.item.type.Material
import kotlinmud.mob.builder.MobBuilder
import kotlinmud.mob.model.MobRespawn
import kotlinmud.mob.race.impl.Canid
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.Gender
import kotlinmud.mob.type.JobType
import kotlinmud.mob.type.MobCanonicalId
import kotlinmud.respawn.helper.respawn
import kotlinmud.room.builder.RoomBuilder
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
    val builder = RoomBuilder(roomService).also {
        it.area = Area.LorimirForest
        it.name = "Deep in the heart of Lorimir Forest."
        it.description = "foo"
    }

    val room1 = build(builder)
    val room2 = build(builder)
    val room3 = build(builder)
    val room4 = build(builder)
    val room5 = build(builder)

    val room6 = build(
        builder.also {
            it.description = "Around a massive tree."
        }
    )
    val room7 = build(builder)
    val room8 = build(builder)

    builder.also {
        it.name = "A dark forest"
        it.description = "Deep in the heart of Lorimir Forest."
    }
    val room9 = build(
        builder.also {
            it.canonicalId = RoomCanonicalId.PRAETORIAN_CAPTAIN_FOUND
        }
    )
    val matrix = SimpleMatrixService(builder, 100).build(5, 5)

    connect(connection).to(room1, Direction.SOUTH)
        .to(room2, Direction.SOUTH)
        .to(
            listOf(
                Pair(room3, Direction.WEST),
                Pair(room4, Direction.EAST)
            )
        )
    connect(room3).to(room5, Direction.WEST)
        .to(room6, Direction.WEST)
        .to(room7, Direction.SOUTH)
        .to(room8, Direction.WEST)
        .to(room9, Direction.NORTH)
        .to(matrix[0][0], Direction.DOWN)

    respawn(
        ItemAreaRespawn(
            ItemCanonicalId.Mushroom,
            ItemBuilder(itemService)
                .name("a small brown mushroom")
                .description("foo")
                .material(Material.ORGANIC)
                .food(Food.MUSHROOM)
                .canonicalId(ItemCanonicalId.Mushroom),
            Area.LorimirForest,
            10,
        ),
    )

    MobBuilder(mobService).also {
        it.name = "Captain Bartok"
        it.brief = "an imposing figure stands here. Her armor bears the emblem of the Praetorian Guard"
        it.description = "Captain Bartok is here"
        it.gender = Gender.FEMALE
        it.room = room9
        it.level = 10
        it.job = JobType.QUEST
        it.canonicalId = MobCanonicalId.PraetorianCaptainBartok
        it.race = Human()
    }.build()

    respawn(
        MobRespawn(
            MobCanonicalId.SmallFox,
            MobBuilder(mobService).also {
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

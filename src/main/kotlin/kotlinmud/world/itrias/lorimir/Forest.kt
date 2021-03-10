package kotlinmud.world.itrias.lorimir

import kotlinmud.generator.service.SimpleMatrixService
import kotlinmud.mob.builder.MobBuilder
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.Gender
import kotlinmud.mob.type.JobType
import kotlinmud.mob.type.MobCanonicalId
import kotlinmud.room.builder.RoomBuilder
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction
import kotlinmud.type.RoomCanonicalId

fun createLorimirForest(mobService: MobService, roomService: RoomService, connection: Room): Room {
    val builder = RoomBuilder(roomService).also {
        it.area = Area.LorimirForest
        it.name = "Deep in the heart of Lorimir Forest."
        it.description = "foo"
    }

    val room1 = builder.also { it.id = 100 }.build()
    val room2 = builder.also { it.id = 101 }.build()
    val room3 = builder.also { it.id = 102 }.build()
    val room4 = builder.also { it.id = 103 }.build()
    val room5 = builder.also { it.id = 104 }.build()

    val room6 = builder.also {
        it.id = 105
        it.description = "Around a massive tree."
    }.build()
    val room7 = builder.also { it.id = 106 }.build()
    val room8 = builder.also { it.id = 107 }.build()

    builder.also {
        it.name = "A dark forest"
        it.description = "Deep in the heart of Lorimir Forest."
    }
    val room9 = builder.also {
        it.id = 109
        it.canonicalId = RoomCanonicalId.PRAETORIAN_CAPTAIN_FOUND
    }.build()
    val matrix = SimpleMatrixService(builder, 110).build(5, 5)

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

    createGrongokHideout(roomService, matrix[0][4])

    return matrix[2][4]
}

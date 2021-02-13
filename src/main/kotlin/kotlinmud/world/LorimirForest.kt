package kotlinmud.world

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.generator.service.SimpleMatrixService
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.type.JobType
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.helper.RoomBuilder
import kotlinmud.room.helper.connect
import kotlinmud.room.type.Direction
import kotlinmud.type.CanonicalId
import org.jetbrains.exposed.sql.transactions.transaction

fun createLorimirForest(connection: RoomDAO) {
    transaction {
        val builder = RoomBuilder()
            .area("Lorimir Forest")
            .name("Deep in the heart of Lorimir Forest.")
            .description("foo")

        val room1 = builder.build()
        val room2 = builder.build()
        val room3 = builder.build()
        val room4 = builder.build()
        val room5 = builder.build()

        builder.description("Around a massive tree.")
        val room6 = builder.build()
        val room7 = builder.build()
        val room8 = builder.build()

        builder
            .name("A dark forest")
            .description("Deep in the heart of Lorimir Forest.")
        val room9 = builder.canonicalId(CanonicalId.PRAETORIAN_CAPTAIN_FOUND).build()
        val matrix = SimpleMatrixService(builder).build(5, 5)

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

        MobDAO.new {
            name = "Captain Bartok"
            brief = "yolo"
            description = "Captain Bartok is here"
            room = room3
            job = JobType.QUEST
            canonicalId = CanonicalId.PRAETORIAN_CAPTAIN_FOUND
            race = Human()
            isNpc = true
            attributes = AttributesDAO.new {}
        }
    }
}

package kotlinmud.world

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.type.JobType
import kotlinmud.room.dao.RoomDAO
import kotlinmud.type.CanonicalId
import org.jetbrains.exposed.sql.transactions.transaction

fun createLorimirForest(connection: RoomDAO) {
    transaction {
        val room1 = RoomDAO.new {
            name = "Deep in the Lorimir Forest"
            description = "foo"
            area = "Lorimir Forest"
        }
        val room2 = RoomDAO.new {
            name = "Deep in the Lorimir Forest"
            description = "foo"
            area = "Lorimir Forest"
        }
        val room3 = RoomDAO.new {
            name = "Deep in the Lorimir Forest"
            description = "foo"
            canonicalId = CanonicalId.PRAETORIAN_CAPTAIN_FOUND
            area = "Lorimir Forest"
        }

        connection.south = room1
        room1.north = connection
        room1.south = room2
        room2.north = room1
        room2.south = room3
        room3.north = room2

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

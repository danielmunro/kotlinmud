package kotlinmud.world

import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.type.JobType
import kotlinmud.room.dao.RoomDAO
import kotlinmud.type.CanonicalId
import org.jetbrains.exposed.sql.transactions.transaction

fun createLorimirForestOutput() {
    transaction {
        val room1 = RoomDAO.new {
            name = "Around a fire pit"
            description = "foo"
            canonicalId = CanonicalId.PRAETORIAN_RECRUITER_1
        }
        val room2 = RoomDAO.new {
            name = "Outside the camp"
            description = "bar"
            canonicalId = CanonicalId.PRAETORIAN_RECRUITER_2
        }

        room1.north = room2
        room2.south = room1

        MobDAO.new {
            name = "Recruiter Bartok"
            room = room1
            job = JobType.QUEST
            canonicalId = CanonicalId.PRAETORIAN_RECRUITER_1
        }

        MobDAO.new {
            name = "Recruiter Bartok"
            room = room2
            job = JobType.QUEST
            canonicalId = CanonicalId.PRAETORIAN_RECRUITER_2
        }
    }
}

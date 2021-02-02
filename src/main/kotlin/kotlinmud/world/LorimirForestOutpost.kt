package kotlinmud.world

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.type.JobType
import kotlinmud.room.dao.RoomDAO
import kotlinmud.type.CanonicalId
import org.jetbrains.exposed.sql.transactions.transaction

fun createLorimirForestOutpost() {
    transaction {
        val room1 = RoomDAO.new {
            name = "Around a fire pit"
            description = "foo"
            canonicalId = CanonicalId.PRAETORIAN_RECRUITER_1
            area = "Lorimir Forest Outpost"
        }
        val room2 = RoomDAO.new {
            name = "Outside the camp"
            description = "bar"
            canonicalId = CanonicalId.PRAETORIAN_RECRUITER_2
            area = "Lorimir Forest Outpost"
        }

        room1.north = room2
        room2.south = room1

        MobDAO.new {
            name = "Recruiter Bartok"
            brief = "a cloaked figure sits against a log, facing the fire, reading a leaflet"
            description = "Recruiter Bartok is here"
            room = room1
            job = JobType.QUEST
            canonicalId = CanonicalId.PRAETORIAN_RECRUITER_1
            race = Human()
            isNpc = true
            attributes = AttributesDAO.new {}
        }

        MobDAO.new {
            name = "Recruiter Bartok"
            brief = "a cloaked figure sits against a log, facing the fire, reading a leaflet"
            description = "Recruiter Bartok is here"
            room = room2
            job = JobType.QUEST
            canonicalId = CanonicalId.PRAETORIAN_RECRUITER_2
            race = Human()
            isNpc = true
            attributes = AttributesDAO.new {}
        }
    }
}

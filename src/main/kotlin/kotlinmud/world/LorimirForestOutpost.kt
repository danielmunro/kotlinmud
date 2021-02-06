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
            canonicalId = CanonicalId.FIND_RECRUITER_PRAETORIAN_GUARD
            area = "Lorimir Forest Outpost"
        }

        val room2 = RoomDAO.new {
            name = "Outside the camp"
            description = "bar"
            canonicalId = CanonicalId.PRAETORIAN_GUARD_RECRUITER_FOUND
            area = "Lorimir Forest Outpost"
        }

        val room3 = RoomDAO.new {
            name = "Outside the camp"
            description = "bar"
            canonicalId = CanonicalId.FIND_PRAETORIAN_CAPTAIN
            area = "Lorimir Forest Outpost"
        }

        val room4 = RoomDAO.new {
            name = "Outside the camp"
            description = "bar"
            canonicalId = CanonicalId.PRAETORIAN_CAPTAIN_FOUND
            area = "Lorimir Forest Outpost"
        }

        room1.north = room2
        room2.south = room1

        MobDAO.new {
            name = "Recruiter Esmer"
            brief = "a cloaked figure sits against a log, facing the fire, reading a leaflet"
            description = "Recruiter Bartok is here"
            room = room1
            job = JobType.QUEST
            canonicalId = CanonicalId.FIND_RECRUITER_PRAETORIAN_GUARD
            race = Human()
            isNpc = true
            attributes = AttributesDAO.new {}
        }

        MobDAO.new {
            name = "Recruiter Esmer"
            brief = "a cloaked figure sits against a log, facing the fire, reading a leaflet"
            description = "Recruiter Bartok is here"
            room = room2
            job = JobType.QUEST
            canonicalId = CanonicalId.PRAETORIAN_GUARD_RECRUITER_FOUND
            race = Human()
            isNpc = true
            attributes = AttributesDAO.new {}
        }

        MobDAO.new {
            name = "Captain Bartok"
            brief = "yolo"
            description = "Captain Bartok is here"
            room = room3
            job = JobType.QUEST
            canonicalId = CanonicalId.FIND_PRAETORIAN_CAPTAIN
            race = Human()
            isNpc = true
            attributes = AttributesDAO.new {}
        }

        MobDAO.new {
            name = "Captain Bartok"
            brief = "yolo"
            description = "Captain Bartok is here"
            room = room4
            job = JobType.QUEST
            canonicalId = CanonicalId.PRAETORIAN_CAPTAIN_FOUND
            race = Human()
            isNpc = true
            attributes = AttributesDAO.new {}
        }
    }
}

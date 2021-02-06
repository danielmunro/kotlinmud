package kotlinmud.world

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.type.JobType
import kotlinmud.room.dao.RoomDAO
import kotlinmud.type.CanonicalId
import org.jetbrains.exposed.sql.transactions.transaction

fun createLorimirForestOutpost(): RoomDAO {
    return transaction {
        val room1 = RoomDAO.new {
            name = "Around a fire pit"
            description = "A small firepit is here. A sign is here with available quests."
            canonicalId = CanonicalId.FIND_RECRUITER_PRAETORIAN_GUARD
            area = "Lorimir Forest Outpost"
        }

        val room2 = RoomDAO.new {
            name = "Inside a lean-to shelter"
            description = "bar"
            canonicalId = CanonicalId.PRAETORIAN_GUARD_RECRUITER_FOUND
            area = "Lorimir Forest Outpost"
        }

        val room3 = RoomDAO.new {
            name = "A trail near the camp"
            description = "bar"
            area = "Lorimir Forest Outpost"
        }

        val room4 = RoomDAO.new {
            name = "Outside the camp"
            description = "bar"
            area = "Lorimir Forest Outpost"
        }

        room1.north = room2
        room2.south = room1
        room1.east = room3
        room1.south = room4

        MobDAO.new {
            name = "Recruiter Esmer"
            brief = "a cloaked figure sits against a log, facing the fire, reading a leaflet"
            description = "Recruiter Esmer is here"
            room = room2
            job = JobType.QUEST
            canonicalId = CanonicalId.PRAETORIAN_GUARD_RECRUITER_FOUND
            race = Human()
            isNpc = true
            attributes = AttributesDAO.new {}
        }
        return@transaction room4
    }
}

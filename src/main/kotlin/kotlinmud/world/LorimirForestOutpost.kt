package kotlinmud.world

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.type.JobType
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.helper.RoomBuilder
import kotlinmud.room.helper.connect
import kotlinmud.room.type.Direction
import kotlinmud.type.CanonicalId
import org.jetbrains.exposed.sql.transactions.transaction

fun createLorimirForestOutpost(): RoomDAO {
    return transaction {
        val builder = RoomBuilder().area("Lorimir Forest Outpost")

        val room1 = builder.name("Around a fire pit")
            .description("A small firepit is here. A sign is here with available quests.")
            .canonicalId(CanonicalId.FIND_RECRUITER_PRAETORIAN_GUARD)
            .build()

        val room2 = builder.name("Inside a lean-to shelter")
            .description("bar")
            .canonicalId(CanonicalId.PRAETORIAN_GUARD_RECRUITER_FOUND)
            .build()

        val room3 = builder.name("A trail near the camp").build()
        val room4 = builder.name("Outside the camp").build()

        connect(room1)
            .to(
                listOf(
                    Pair(room2, Direction.NORTH),
                    Pair(room3, Direction.EAST),
                    Pair(room4, Direction.SOUTH),
                )
            )

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

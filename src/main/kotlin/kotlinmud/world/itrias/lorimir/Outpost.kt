package kotlinmud.world.itrias.lorimir

import kotlinmud.item.helper.ItemBuilder
import kotlinmud.item.type.Material
import kotlinmud.mob.helper.MobBuilder
import kotlinmud.mob.race.impl.Giant
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.type.JobType
import kotlinmud.mob.type.MobCanonicalId
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.helper.RoomBuilder
import kotlinmud.room.helper.connect
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction
import kotlinmud.type.CanonicalId
import org.jetbrains.exposed.sql.transactions.transaction

fun createLorimirForestOutpost(): RoomDAO {
    return transaction {
        val builder = RoomBuilder().area(Area.LorimirForestOutpost)

        val room1 = builder.name("Around a fire pit")
            .description(
"""A circular cobblestone fire-pit serves as the centerpiece for the modest outpost that surrounds you.

A sign flickers against the light of the fire.""".trimMargin()
            )
            .canonicalId(CanonicalId.FIND_RECRUITER_PRAETORIAN_GUARD)
            .build()

        ItemBuilder("a cobblestone fire-pit")
            .description("a fire emanates from the circular pit.")
            .room(room1)
            .cannotOwn()
            .material(Material.STONE)
            .build()

        ItemBuilder("a large wooden sign on a post")
            .description(
"""The sign reads:

+-------------------------------------------------+
|                                                 |
|        Type `quest list` in order to see        |
|        an available quest.                      |
|                                                 |
+-------------------------------------------------+"""
            )
            .room(room1)
            .cannotOwn()
            .material(Material.WOOD)
            .build()

        val room2 = builder.name("Inside a lean-to shelter")
            .description("bar")
            .canonicalId(CanonicalId.PRAETORIAN_GUARD_RECRUITER_FOUND)
            .build()

        val room3 = builder.name("A blacksmith shack").build()
        val room4 = builder.name("A trail near the camp").build()
        val room5 = builder.name("Outside the camp").build()

        connect(room1)
            .to(
                listOf(
                    Pair(room2, Direction.NORTH),
                    Pair(room3, Direction.WEST),
                    Pair(room4, Direction.EAST),
                    Pair(room5, Direction.SOUTH),
                )
            )

        MobBuilder()
            .name("Blacksmith Felig")
            .brief("a blacksmith stands over a forge, monitoring his work")
            .description("a large giant is here, forging a weapon")
            .room(room3)
            .job(JobType.SHOPKEEPER)
            .race(Giant())
            .build()

        MobBuilder()
            .name("Recruiter Esmer")
            .brief("a cloaked figure sits against a log, facing the fire, reading a leaflet")
            .description("Recruiter Esmer is here")
            .room(room2)
            .job(JobType.QUEST)
            .race(Human())
            .canonicalId(MobCanonicalId.PraetorianRecruiterEsmer)
            .build()

        return@transaction room4
    }
}

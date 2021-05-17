package kotlinmud.world.itrias.lorimir

import kotlinmud.mob.builder.MobBuilder
import kotlinmud.mob.model.MobRespawn
import kotlinmud.mob.race.impl.Ogre
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.MobCanonicalId
import kotlinmud.respawn.helper.respawn
import kotlinmud.room.builder.RoomBuilder
import kotlinmud.room.builder.build
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction

fun createGrongokHideout(mobService: MobService, roomService: RoomService, connector: Room) {
    val builder = RoomBuilder(roomService).also {
        it.name = "entrance to a cave"
        it.description = "a cave"
        it.area = Area.GrongokHideout
    }

    val room1 = build(builder)
    val room2 = build(
        builder.also {
            it.name = "deep in a cave"
        }
    )

    val room3 = build(builder)
    val room4 = build(builder)
    val room5 = build(builder)

    respawn(
        MobRespawn(
            MobCanonicalId.Grongok,
            MobBuilder(mobService).also {
                it.name = "Grongok"
                it.brief = "a wild looking ogre is here"
                it.description = "Grongok the wild ogre is here."
                it.level = 3
                it.race = Ogre()
                it.canonicalId = MobCanonicalId.Grongok
            },
            Area.GrongokHideout,
            1
        )
    )

    connect(connector).to(room1).to(room2) to mapOf(
        Pair(room3, Direction.SOUTH),
        Pair(room4, Direction.EAST),
        Pair(room5, Direction.WEST),
    )
}

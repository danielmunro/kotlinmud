package kotlinmud.world.itrias.lorimir

import kotlinmud.generator.service.SimpleMatrixService
import kotlinmud.mob.race.impl.Amphibian
import kotlinmud.mob.race.impl.Ogre
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.MobCanonicalId
import kotlinmud.respawn.helper.respawn
import kotlinmud.respawn.model.MobRespawn
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
    val matrix = SimpleMatrixService(builder).build(2, 2)
    val room5 = build(builder)

    respawn(
        MobRespawn(
            mobService.builder().also {
                it.name = "Grongok"
                it.brief = "a wild looking ogre is here"
                it.description = "Grongok the wild ogre is here."
                it.level = 8
                it.race = Ogre()
                it.canonicalId = MobCanonicalId.Grongok
            },
            Area.GrongokHideout,
            1
        )
    )

    respawn(
        MobRespawn(
            mobService.builder().also {
                it.name = "a warty toad"
                it.brief = "a warty toad is here, looking for water"
                it.description = "tbd"
                it.level = 3
                it.race = Amphibian()
                it.canonicalId = MobCanonicalId.CaveToad
            },
            Area.GrongokHideout,
            3
        )
    )

    connect(connector)
        .toRoom(room1)
        .toRoom(room2)
        .toRoom(
            listOf(
                Pair(room3, Direction.SOUTH),
                Pair(room4, Direction.EAST),
            )
        )
        .toRoom(matrix[0][0])

    connect(matrix[1][1]).toRoom(room5)
}

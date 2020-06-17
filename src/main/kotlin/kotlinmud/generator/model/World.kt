package kotlinmud.generator.model

import kotlinmud.fs.loader.area.model.reset.MobReset
import kotlinmud.generator.type.Matrix3D
import kotlinmud.mob.model.Mob
import kotlinmud.room.model.Room

class World(
    val rooms: List<Room>,
    val matrix3D: Matrix3D,
    val mobs: List<Mob>,
    val mobResets: List<MobReset>
)

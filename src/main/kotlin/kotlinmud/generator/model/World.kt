package kotlinmud.generator.model

import kotlinmud.fs.loader.area.model.reset.MobReset
import kotlinmud.generator.type.Matrix3D
import kotlinmud.mob.dao.MobDAO
import kotlinmud.room.dao.RoomDAO

class World(
    val rooms: List<RoomDAO>,
    val matrix3D: Matrix3D,
    val mobs: List<MobDAO>,
    val mobResets: List<MobReset>
)

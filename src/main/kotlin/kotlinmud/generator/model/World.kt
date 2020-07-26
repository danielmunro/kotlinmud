package kotlinmud.generator.model

import kotlinmud.generator.type.Matrix3D
import kotlinmud.room.dao.RoomDAO

class World(
    val rooms: List<RoomDAO>,
    val matrix3D: Matrix3D
)

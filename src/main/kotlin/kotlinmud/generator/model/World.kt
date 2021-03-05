package kotlinmud.generator.model

import kotlinmud.generator.type.Matrix3D
import kotlinmud.room.model.Room

class World(
        val rooms: List<Room>,
        val matrix3D: Matrix3D
)

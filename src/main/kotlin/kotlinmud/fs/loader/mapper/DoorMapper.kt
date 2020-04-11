package kotlinmud.fs.loader.mapper

import kotlinmud.fs.loader.model.DoorModel
import kotlinmud.world.room.exit.Door

class DoorMapper(private val doors: List<DoorModel>) {
    fun map(): List<Door> {
        var i = 1
        return doors.map {
            Door(i++, it.name, it.description, it.disposition)
        }
    }
}
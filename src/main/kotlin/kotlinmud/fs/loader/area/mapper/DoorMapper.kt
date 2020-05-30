package kotlinmud.fs.loader.area.mapper

import kotlinmud.fs.loader.area.model.DoorModel
import kotlinmud.room.model.Door

class DoorMapper(private val doors: List<DoorModel>) {
    fun map(): List<Door> {
        var i = 1
        return doors.map {
            Door(i++, it.name, it.description, it.disposition)
        }
    }
}

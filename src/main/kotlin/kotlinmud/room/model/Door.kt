package kotlinmud.room.model

import kotlinmud.room.type.DoorDisposition

class Door(
    val name: String,
    val description: String,
    val defaultDisposition: DoorDisposition,
    var disposition: DoorDisposition
) {
    override fun toString(): String {
        return name
    }
}
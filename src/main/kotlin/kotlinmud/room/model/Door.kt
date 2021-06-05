package kotlinmud.room.model

import kotlinmud.room.type.DoorDisposition
import java.util.UUID

class Door(
    val name: String,
    val description: String,
    val defaultDisposition: DoorDisposition,
    val key: UUID,
    var disposition: DoorDisposition = defaultDisposition,
) {
    override fun toString(): String {
        return name
    }
}

package kotlinmud.room.model

import kotlinmud.room.type.DoorDisposition

class Door(
    val name: String,
    val brief: String,
    val description: String,
    val defaultDisposition: DoorDisposition,
    val keyId: Int,
    var disposition: DoorDisposition = defaultDisposition,
) {
    override fun toString(): String {
        return name
    }
}

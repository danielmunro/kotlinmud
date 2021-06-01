package kotlinmud.room.model

import kotlinmud.item.type.ItemCanonicalId
import kotlinmud.room.type.DoorDisposition

class Door(
    val name: String,
    val description: String,
    val defaultDisposition: DoorDisposition,
    val key: ItemCanonicalId,
    var disposition: DoorDisposition = defaultDisposition,
) {
    override fun toString(): String {
        return name
    }
}

package kotlinmud.world.room

import kotlinmud.data.Row
import kotlinmud.item.HasInventory
import kotlinmud.world.room.exit.DoorDisposition
import kotlinmud.world.room.exit.Exit

data class Room(
    override val id: Int,
    val area: String,
    val name: String,
    val description: String,
    val regen: RegenLevel,
    val isIndoor: Boolean
) : Row, HasInventory {
    val exits: MutableList<Exit> = mutableListOf()

    fun openExits(): List<Exit> {
        return exits.filter { it.door == null || it.door.disposition == DoorDisposition.OPEN }
    }
}

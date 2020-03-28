package kotlinmud.room

import kotlinmud.data.Row
import kotlinmud.item.Inventory
import kotlinmud.room.exit.Exit

data class Room(
    override val id: Int,
    val area: String,
    val name: String,
    val description: String,
    val regen: RegenLevel,
    val isIndoor: Boolean
) : Row {
    val inventory: Inventory = Inventory()
    val exits: MutableList<Exit> = mutableListOf()
}

package kotlinmud.room

import kotlinmud.item.Inventory
import kotlinmud.room.exit.Exit

class Room(
    val name: String,
    val description: String,
    val inventory: Inventory,
    val exits: MutableList<Exit>
)

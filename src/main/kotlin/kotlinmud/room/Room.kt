package kotlinmud.room

import kotlinmud.exit.Exit
import kotlinmud.item.Inventory

class Room(
    val name: String,
    val description: String,
    val inventory: Inventory,
    val exits: MutableList<Exit>
)
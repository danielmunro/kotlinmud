package kotlinmud.room

import kotlinmud.item.Inventory
import kotlinmud.room.exit.Exit

class Room(
    val id: Int,
    val name: String,
    val description: String,
    val inventory: Inventory = Inventory(),
    val exits: MutableList<Exit> = mutableListOf()
)

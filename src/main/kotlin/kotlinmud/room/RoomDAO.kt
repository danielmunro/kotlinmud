package kotlinmud.room

import kotlinmud.exit.Exit
import kotlinmud.exit.Exits
import kotlinmud.item.Inventories
import kotlinmud.item.Inventory
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object Rooms: IntIdTable() {
    val name = varchar("name", 50)
    val description = text("description")
    val exits = reference("exits", Exits)
    val inventory = reference("inventory", Inventories)
}

class Room(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<Room>(
        Rooms
    )

    var name by Rooms.name
    var description by Rooms.description
    val exits by Exit referrersOn Rooms.exits
    val inventory by Inventory referrersOn Rooms.inventory
}
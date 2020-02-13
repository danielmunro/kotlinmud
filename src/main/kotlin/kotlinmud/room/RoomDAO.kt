package kotlinmud.room

import kotlinmud.exit.ExitEntity
import kotlinmud.exit.Exits
import kotlinmud.item.Inventories
import kotlinmud.item.InventoryEntity
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object Rooms : IntIdTable() {
    val uuid = uuid("uuid")
    val name = varchar("name", 50)
    val description = text("description")
    val inventory = reference("inventory", Inventories)
}

class RoomEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RoomEntity>(
        Rooms
    )

    var uuid by Rooms.uuid
    var name by Rooms.name
    var description by Rooms.description
    val exits by ExitEntity referrersOn Exits.room
    var inventory by InventoryEntity referencedOn Rooms.inventory
}

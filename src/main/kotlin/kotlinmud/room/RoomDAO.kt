package kotlinmud.room

import kotlinmud.room.exit.ExitEntity
import kotlinmud.room.exit.ExitTable
import kotlinmud.item.Inventories
import kotlinmud.item.InventoryEntity
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object RoomTable : IntIdTable() {
    val uuid = uuid("uuid")
    val name = varchar("name", 50)
    val description = text("description")
    val inventory = reference("inventory", Inventories)
}

class RoomEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RoomEntity>(
        RoomTable
    )

    var uuid by RoomTable.uuid
    var name by RoomTable.name
    var description by RoomTable.description
    val exits by ExitEntity referrersOn ExitTable.room
    var inventory by InventoryEntity referencedOn RoomTable.inventory

    override fun toString(): String {
        return name
    }
}

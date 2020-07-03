package kotlinmud.item.table

import kotlinmud.attributes.table.Attributes
import kotlinmud.mob.table.Mobs
import kotlinmud.room.table.Rooms
import org.jetbrains.exposed.dao.IntIdTable

object Items : IntIdTable()  {
    val mobInventoryId = reference("mobInventoryId", Mobs.id)
    val mobEquippedId = reference("mobEquippedId", Mobs.id)
    val roomId = reference("room_id", Rooms.id)
    val name = varchar("name", 255)
    val description = text("description")
    val type = varchar("type", 50)
    val worth = integer("worth")
    val level = integer("level")
    val weight = double("weight")
    val material = varchar("material", 50)
    val position = varchar("position", 50).nullable()
    val attackVerb = varchar("attackVerb", 50).nullable()
    val damageType = varchar("damageType", 50).nullable()
    val drink = varchar("drink", 50).nullable()
    val food = varchar("food", 50).nullable()
    val quantity = integer("quantity").nullable()
    val decayTimer = integer("decayTimer").nullable()
    val canOwn = bool("canOwn")
    val attributesId = reference("attributes", Attributes)
    // affects
    // inventory
}

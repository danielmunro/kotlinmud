package kotlinmud.item.table

import kotlinmud.affect.table.Affects
import kotlinmud.attributes.table.Attributes
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.mob.table.Mobs
import kotlinmud.room.table.Doors
import kotlinmud.room.table.Rooms
import org.jetbrains.exposed.dao.IntIdTable

object Items : IntIdTable() {
    val name = varchar("name", 255)
    val description = text("description")
    val type = varchar("type", 50).default(ItemType.OTHER.toString())
    val worth = integer("worth").default(0)
    val level = integer("level").default(1)
    val weight = double("weight").default(1.0)
    val material = varchar("material", 50).default(Material.ORGANIC.toString())
    val isContainer = bool("isContainer").default(false)
    val position = varchar("position", 50).nullable()
    val attackVerb = varchar("attackVerb", 50).nullable()
    val damageType = varchar("damageType", 50).nullable()
    val drink = varchar("drink", 50).nullable()
    val food = varchar("food", 50).nullable()
    val quantity = integer("quantity").nullable()
    val decayTimer = integer("decayTimer").nullable()
    val canOwn = bool("canOwn").default(true)
    val maxItems = integer("maxItems").nullable()
    val maxWeight = integer("maxWeight").nullable()
    val attributesId = reference("attributes", Attributes)
    val affects = reference("affects", Affects).nullable()
    val mobInventoryId = reference("mobInventoryId", Mobs).nullable()
    val mobEquippedId = reference("mobEquippedId", Mobs).nullable()
    val roomId = reference("roomId", Rooms).nullable()
    val itemId = reference("itemId", Items).nullable()
    val doorKeyId = reference("doorKeyId", Doors).nullable()
}

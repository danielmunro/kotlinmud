package kotlinmud.item.dao

import kotlinmud.affect.dao.AffectDAO
import kotlinmud.affect.table.Affects
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.type.HasAttributes
import kotlinmud.helper.Noun
import kotlinmud.item.table.Items
import kotlinmud.item.type.Drink
import kotlinmud.item.type.Food
import kotlinmud.item.type.HasInventory
import kotlinmud.item.type.ItemCanonicalId
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.room.dao.RoomDAO
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.sql.transactions.transaction

class ItemDAO(id: EntityID<Int>) : IntEntity(id), HasAttributes, Noun, HasInventory {
    companion object : IntEntityClass<ItemDAO>(Items)

    override var name by Items.name
    override var description by Items.description
    var type by Items.type.transform(
        { it.toString() },
        { ItemType.valueOf(it) }
    )
    var worth by Items.worth
    var level by Items.level
    var weight by Items.weight
    var material by Items.material.transform(
        { it.toString() },
        { Material.valueOf(it) }
    )
    var isContainer by Items.isContainer
    var position by Items.position.transform(
        { it.toString() },
        { it?.let { Position.valueOf(it) } }
    )
    var attackVerb by Items.attackVerb
    var damageType by Items.damageType.transform(
        { it.toString() },
        { it?.let { DamageType.valueOf(it) } }
    )
    var drink by Items.drink.transform(
        { it.toString() },
        { it?.let { Drink.valueOf(it) } }
    )
    var food by Items.food.transform(
        { it.toString() },
        { it?.let { Food.valueOf(it) } }
    )
    var quantity by Items.quantity
    var decayTimer by Items.decayTimer
    var canOwn by Items.canOwn
    override var maxItems by Items.maxItems
    override var maxWeight by Items.maxWeight
    var canonicalId by Items.canonicalId.transform(
        { it.toString() },
        { it?.let { ItemCanonicalId.valueOf(it) } }
    )
    override var attributes by AttributesDAO optionalReferencedOn Items.attributesId
    override val affects by AffectDAO optionalReferrersOn Affects.itemId
    var mobInventory by MobDAO optionalReferencedOn Items.mobInventoryId
    var mobEquipped by MobDAO optionalReferencedOn Items.mobEquippedId
    var room by RoomDAO optionalReferencedOn Items.roomId
    override val items by ItemDAO optionalReferrersOn Items.itemId
    var container by ItemDAO optionalReferencedOn Items.itemId
//    var doorKey by ItemDAO optionalReferencedOn Doors.keyItemId

    fun isVisible(): Boolean {
        return transaction { affects.find { it.type == AffectType.INVISIBILITY } } == null
    }

    fun isFood(): Boolean {
        return food != null
    }

    override fun toString(): String {
        return name
    }
}

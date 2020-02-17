package kotlinmud.mob

import kotlinmud.attributes.Attributes
import kotlinmud.attributes.AttributesEntity
import kotlinmud.item.Inventories
import kotlinmud.item.InventoryEntity
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object Mobs : IntIdTable() {
    val name = varchar("name", 50)
    val description = text("description")
    val disposition = varchar("disposition", 50)
    val hp = integer("hp")
    val mana = integer("mana")
    val mv = integer("mv")
    val inventory = reference("inventory", Inventories)
    val attributes = reference("attributes", Attributes)
}

class MobEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MobEntity>(
        Mobs
    )

    var name by Mobs.name
    var description by Mobs.description
    var disposition by Mobs.disposition
    var hp by Mobs.hp
    var mana by Mobs.mana
    var mv by Mobs.mv
    var inventory by InventoryEntity referencedOn Mobs.inventory
    var attributes by AttributesEntity referencedOn Mobs.attributes

    fun getAttacks(): List<AttackType> {
        return arrayListOf(AttackType.FIRST)
    }

    fun getDamageType(): DamageType {
        return DamageType.POUND
    }

    override fun toString(): String {
        return name
    }
}

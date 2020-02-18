package kotlinmud.mob

import kotlinmud.attributes.AttributesTable
import kotlinmud.attributes.AttributesEntity
import kotlinmud.item.Inventories
import kotlinmud.item.InventoryEntity
import kotlinmud.mob.fight.AttackType
import kotlinmud.mob.fight.DamageType
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object MobTable : IntIdTable() {
    val name = varchar("name", 50)
    val description = text("description")
    val disposition = varchar("disposition", 50)
    val hp = integer("hp")
    val mana = integer("mana")
    val mv = integer("mv")
    val inventory = reference("inventory", Inventories)
    val attributes = reference("attributes", AttributesTable)
}

class MobEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MobEntity>(
        MobTable
    )

    var name by MobTable.name
    var description by MobTable.description
    var disposition by MobTable.disposition
    var hp by MobTable.hp
    var mana by MobTable.mana
    var mv by MobTable.mv
    var inventory by InventoryEntity referencedOn MobTable.inventory
    var attributes by AttributesEntity referencedOn MobTable.attributes

    fun getAttacks(): List<AttackType> {
        return arrayListOf(AttackType.FIRST)
    }

    fun getDamageType(): DamageType {
        return DamageType.POUND
    }

    fun isSleeping(): Boolean {
        return disposition == Disposition.SLEEPING.value
    }

    fun isIncapacitated(): Boolean {
        return disposition == Disposition.DEAD.value
    }

    override fun toString(): String {
        return name
    }
}

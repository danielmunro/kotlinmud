package kotlinmud.attributes

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object AttributesTable : IntIdTable() {
    val hp = integer("hp").default(0)
    val mana = integer("mana").default(0)
    val mv = integer("mv").default(0)
    val str = integer("str").default(0)
    val int = integer("int").default(0)
    val wis = integer("wis").default(0)
    val dex = integer("dex").default(0)
    val con = integer("con").default(0)
    val hit = integer("hit").default(0)
    val dam = integer("dam").default(0)
    val acBash = integer("acBash").default(0)
    val acPierce = integer("acPierce").default(0)
    val acSlash = integer("acSlash").default(0)
    val acMagic = integer("acMagic").default(0)
}

class AttributesEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AttributesEntity>(AttributesTable)

    var hp by AttributesTable.hp
    var mana by AttributesTable.mana
    var mv by AttributesTable.mv
    var str by AttributesTable.str
    var int by AttributesTable.int
    var wis by AttributesTable.wis
    var dex by AttributesTable.dex
    var con by AttributesTable.con
    var hit by AttributesTable.hit
    var dam by AttributesTable.dam
    var acBash by AttributesTable.acBash
    var acPierce by AttributesTable.acPierce
    var acSlash by AttributesTable.acSlash
    var acMagic by AttributesTable.acMagic
}

package kotlinmud.attributes

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object Attributes : IntIdTable() {
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
    companion object : IntEntityClass<AttributesEntity>(Attributes)

    var hp by Attributes.hp
    var mana by Attributes.mana
    var mv by Attributes.mv
    var str by Attributes.str
    var int by Attributes.int
    var wis by Attributes.wis
    var dex by Attributes.dex
    var con by Attributes.con
    var hit by Attributes.hit
    var dam by Attributes.dam
    var acBash by Attributes.acBash
    var acPierce by Attributes.acPierce
    var acSlash by Attributes.acSlash
    var acMagic by Attributes.acMagic
}

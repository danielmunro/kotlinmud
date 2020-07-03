package kotlinmud.attributes.table

import org.jetbrains.exposed.dao.IntIdTable

object Attributes : IntIdTable() {
    val hp = integer("hp")
    val mana = integer("mana")
    val mv = integer("mv")
    val strength = integer("strength")
    val intelligence = integer("intelligence")
    val wisdom = integer("wisdom")
    val dexterity = integer("dexterity")
    val constitution = integer("constitution")
    val hit = integer("hit")
    val dam = integer("dam")
    val acBash = integer("acBash")
    val acSlash = integer("acSlash")
    val acPierce = integer("acPierce")
    val acMagic = integer("acMagic")
}

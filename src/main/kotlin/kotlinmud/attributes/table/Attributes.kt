package kotlinmud.attributes.table

import kotlinmud.player.table.MobCards
import org.jetbrains.exposed.dao.IntIdTable

object Attributes : IntIdTable() {
    val hp = integer("hp").default(0)
    val mana = integer("mana").default(0)
    val mv = integer("mv").default(0)
    val strength = integer("strength").default(0)
    val intelligence = integer("intelligence").default(0)
    val wisdom = integer("wisdom").default(0)
    val dexterity = integer("dexterity").default(0)
    val constitution = integer("constitution").default(0)
    val hit = integer("hit").default(0)
    val dam = integer("dam").default(0)
    val acBash = integer("acBash").default(0)
    val acSlash = integer("acSlash").default(0)
    val acPierce = integer("acPierce").default(0)
    val acMagic = integer("acMagic").default(0)
    val mobCardId = reference("mobCardId", MobCards)
}

package kotlinmud.mob

import kotlinmud.attributes.Attribute
import kotlinmud.attributes.Attributes
import kotlinmud.item.Inventory
import kotlinmud.item.Item
import kotlinmud.mob.fight.AttackType
import kotlinmud.mob.fight.DamageType

class Mob(
    val name: String,
    var description: String,
    var disposition: Disposition,
    var hp: Int,
    var mana: Int,
    var mv: Int,
    val attributes: Attributes,
    val inventory: Inventory,
    val equipped: Inventory
) {
    fun isSleeping(): Boolean {
        return disposition == Disposition.SLEEPING
    }

    fun isIncapacitated(): Boolean {
        return disposition == Disposition.DEAD
    }

    fun isStanding(): Boolean {
        return disposition == Disposition.FIGHTING || disposition == Disposition.STANDING
    }

    fun getAttacks(): List<AttackType> {
        return arrayListOf(AttackType.FIRST)
    }

    fun getDamageType(): DamageType {
        return DamageType.POUND
    }

    fun calc(attribute: Attribute): Int {
        return when(attribute) {
            Attribute.HP -> attributes.hp + accumulate { it.attributes.hp }
            Attribute.MANA -> attributes.mana + accumulate { it.attributes.mana }
            Attribute.MV -> attributes.mv + accumulate { it.attributes.mv }
            Attribute.STR -> attributes.str + accumulate { it.attributes.str }
            Attribute.INT -> attributes.int + accumulate { it.attributes.int }
            Attribute.WIS -> attributes.wis + accumulate { it.attributes.wis }
            Attribute.DEX -> attributes.dex + accumulate { it.attributes.dex }
            Attribute.CON -> attributes.con + accumulate { it.attributes.con }
            Attribute.HIT -> attributes.hit + accumulate { it.attributes.hit }
            Attribute.DAM -> attributes.dam + accumulate { it.attributes.dam }
            Attribute.AC_BASH -> attributes.acBash + accumulate { it.attributes.acBash }
            Attribute.AC_PIERCE -> attributes.acPierce + accumulate { it.attributes.acPierce }
            Attribute.AC_SLASH -> attributes.acSlash + accumulate { it.attributes.acSlash }
            Attribute.AC_MAGIC -> attributes.acMagic + accumulate { it.attributes.acMagic }
        }
    }

    override fun toString(): String {
        return name
    }

    private fun accumulate(accumulator: (Item) -> Int): Int {
        return equipped.items.map(accumulator).reduce { acc: Int, it: Int -> acc + it }
    }
}
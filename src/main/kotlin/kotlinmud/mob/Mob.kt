package kotlinmud.mob

import kotlinmud.attributes.Attributes
import kotlinmud.item.Inventory
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
    val inventory: Inventory
) {
    fun isSleeping(): Boolean {
        return disposition == Disposition.SLEEPING
    }

    fun isIncapacitated(): Boolean {
        return disposition == Disposition.DEAD
    }

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

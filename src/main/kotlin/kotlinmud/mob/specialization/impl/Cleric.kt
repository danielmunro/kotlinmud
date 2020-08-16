package kotlinmud.mob.specialization.impl

import kotlinmud.attributes.type.Attribute
import kotlinmud.item.type.Weapon
import kotlinmud.mob.specialization.type.Specialization

class Cleric : Specialization {
    override val name = "cleric"
    override val primeAttribute = Attribute.WIS
    override val secondAttribute = Attribute.INT
    override val startingWeapon = Weapon.MACE
    override val dam: Int = 2
    override val hpMin = 8
    override val hpMax = 12
    override val manaMin = 7
    override val manaMax = 10
}

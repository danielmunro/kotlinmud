package kotlinmud.mob.specialization.impl

import kotlinmud.attributes.type.Attribute
import kotlinmud.item.type.Weapon
import kotlinmud.mob.specialization.type.Specialization

class Mage : Specialization {
    override val name = "mage"
    override val primeAttribute = Attribute.INT
    override val secondAttribute = Attribute.WIS
    override val startingWeapon = Weapon.WAND
    override val dam: Int = 1
    override val hpMin = 4
    override val hpMax = 6
    override val manaMin = 12
    override val manaMax = 16
}

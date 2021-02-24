package kotlinmud.mob.specialization.impl

import kotlinmud.attributes.type.Attribute
import kotlinmud.item.type.Weapon
import kotlinmud.mob.specialization.type.Specialization
import kotlinmud.mob.specialization.type.SpecializationType

class Thief : Specialization {
    override val name = "thief"
    override val primeAttribute = Attribute.DEX
    override val secondAttribute = Attribute.INT
    override val startingWeapon = Weapon.DAGGER
    override val dam: Int = 3
    override val hpMin = 8
    override val hpMax = 12
    override val manaMin = 6
    override val manaMax = 10
    override val type = SpecializationType.THIEF
}

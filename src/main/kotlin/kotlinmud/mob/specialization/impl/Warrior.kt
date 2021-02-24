package kotlinmud.mob.specialization.impl

import kotlinmud.attributes.type.Attribute
import kotlinmud.item.type.Weapon
import kotlinmud.mob.specialization.type.Specialization
import kotlinmud.mob.specialization.type.SpecializationType

class Warrior : Specialization {
    override val name = "warrior"
    override val primeAttribute = Attribute.STR
    override val secondAttribute = Attribute.CON
    override val startingWeapon = Weapon.SWORD
    override val dam: Int = 4
    override val hpMin = 10
    override val hpMax = 15
    override val manaMin = 4
    override val manaMax = 6
    override val type = SpecializationType.WARRIOR
}

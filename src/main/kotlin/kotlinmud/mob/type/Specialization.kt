package kotlinmud.mob.type

import kotlinmud.attributes.type.Attribute
import kotlinmud.item.type.Weapon

interface Specialization {
    val name: String
    val primeAttribute: Attribute
    val secondAttribute: Attribute
    val startingWeapon: Weapon
    val dam: Int
    val hpMin: Int
    val hpMax: Int
    val manaMin: Int
    val manaMax: Int
}

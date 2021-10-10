package kotlinmud.startup.validator

import kotlinmud.attributes.type.Attribute
import kotlinmud.item.model.Item
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Position
import kotlinmud.startup.exception.ItemValidatorException

class ItemValidator(private val item: Item) : Validator {
    override fun validate() {
        try {
            when (item.type) {
                ItemType.EQUIPMENT -> validateEquipment()
                else -> return
            }
        } catch (e: NullPointerException) {
            throw ItemValidatorException()
        }
    }

    private fun validateEquipment() {
        item.position!!.let {
            if (it == Position.WEAPON) {
                validateWeapon()
            }
        }
    }

    private fun validateWeapon() {
        item.weaponType!!
        item.damageType!!
        item.attackVerb!!
        item.attributes[Attribute.HIT]!!
        item.attributes[Attribute.DAM]!!
    }
}

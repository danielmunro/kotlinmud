package kotlinmud.item

import kotlinmud.Noun
import kotlinmud.affect.AffectInstance
import kotlinmud.affect.AffectType
import kotlinmud.attributes.Attribute
import kotlinmud.attributes.Attributes
import kotlinmud.attributes.HasAttributes
import kotlinmud.data.Row
import kotlinmud.loader.model.Model

class Item(
    override val id: Int,
    override val name: String,
    override val description: String,
    val value: Int,
    val level: Int,
    val weight: Double = 1.0,
    override val attributes: Attributes,
    val material: Material,
    val position: Position,
    override val affects: MutableList<AffectInstance>,
    val inventory: Inventory?,
    val drink: Drink,
    val food: Food,
    var quantity: Int
) : HasAttributes, Noun, Row {
    override fun toString(): String {
        return name
    }

    fun isAffectedBy(affectType: AffectType): Boolean {
        return affects.find { it.affectType == affectType } != null
    }

    fun copy(): Item {
        return Item(
            id,
            name,
            description,
            value,
            level,
            weight,
            attributes.copy(),
            material,
            position,
            mutableListOf(),
            Inventory(),
            drink,
            food,
            quantity
        )
    }

    class Builder(override val id: Int, var name: String) : Model {
        var description = ""
        var value = 0
        var level = 1
        var weight = 1.0
        var material = Material.ORGANIC
        var position = Position.NONE
        var attributes = Attributes.Builder()
        var inventory: Inventory? = null
        var affects: MutableList<AffectInstance> = mutableListOf()
        var drink: Drink = Drink.NONE
        var food: Food = Food.NONE
        var quantity: Int = -1

        fun setName(value: String): Builder {
            name = value
            return this
        }

        fun setDescription(value: String): Builder {
            description = value
            return this
        }

        fun setValue(theValue: Int): Builder {
            value = theValue
            return this
        }

        fun setLevel(value: Int): Builder {
            level = value
            return this
        }

        fun setWeight(value: Double): Builder {
            weight = value
            return this
        }

        fun setMaterial(value: Material): Builder {
            material = value
            return this
        }

        fun setPosition(value: Position): Builder {
            position = value
            return this
        }

        fun setAttributes(value: Attributes.Builder): Builder {
            attributes = value
            return this
        }

        fun setAttribute(attribute: Attribute, value: Int): Builder {
            attributes.setAttribute(attribute, value)
            return this
        }

        fun addAffect(affect: AffectInstance): Builder {
            affects.add(affect)
            return this
        }

        fun setDrink(value: Drink): Builder {
            drink = value
            return this
        }

        fun setFood(value: Food): Builder {
            food = value
            return this
        }

        fun setQuantity(value: Int): Builder {
            quantity = value
            return this
        }

        fun makeContainer(): Builder {
            inventory = Inventory()
            return this
        }

        fun build(): Item {
            return Item(
                id,
                name,
                description,
                value,
                level,
                weight,
                attributes.build(),
                material,
                position,
                affects,
                inventory,
                drink,
                food,
                quantity
            )
        }
    }
}

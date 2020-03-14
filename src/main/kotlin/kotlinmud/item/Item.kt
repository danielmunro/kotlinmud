package kotlinmud.item

import kotlinmud.Noun
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
    val inventory: Inventory?
) : HasAttributes, Noun, Row {
    override fun toString(): String {
        return name
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
            Inventory()
        )
    }

    class Builder(override val id: Int, val name: String) : Model {
        var description = ""
        var value = 0
        var level = 1
        var weight = 1.0
        var material = Material.ORGANIC
        var position = Position.NONE
        var attributes = Attributes()
        var inventory: Inventory? = null

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

        fun setAttributes(value: Attributes): Builder {
            attributes = value
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
                attributes,
                material,
                position,
                inventory
            )
        }
    }
}

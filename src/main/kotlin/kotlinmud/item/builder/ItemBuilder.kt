package kotlinmud.item.builder

import kotlinmud.affect.model.Affect
import kotlinmud.attributes.type.Attribute
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.Drink
import kotlinmud.item.type.Food
import kotlinmud.item.type.ItemCanonicalId
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.room.model.Room

class ItemBuilder(private val itemService: ItemService) {
    lateinit var type: ItemType
    lateinit var name: String
    lateinit var description: String
    var worth: Int = 1
    var level: Int = 1
    var weight: Double = 1.0
    lateinit var material: Material
    var isContainer = false
    var canOwn = true
    var affects: List<Affect> = listOf()
    var canonicalId: ItemCanonicalId? = null
    var position: Position? = null
    var attackVerb: String? = null
    var damageType: DamageType? = null
    var drink: Drink? = null
    var food: Food? = null
    var quantity: Int? = null
    var decayTimer: Int? = null
    var maxItems: Int? = null
    var maxWeight: Int? = null
    var attributes: Map<Attribute, Int>? = null
    var items: List<Item>? = null
    var room: Room? = null

    fun type(value: ItemType): ItemBuilder {
        type = value

        if (type == ItemType.CONTAINER) {
            items = listOf()
            isContainer = true
        }

        return this
    }

    fun name(value: String): ItemBuilder {
        name = value
        return this
    }

    fun description(value: String): ItemBuilder {
        description = value
        return this
    }

    fun worth(value: Int): ItemBuilder {
        worth = value
        return this
    }

    fun level(value: Int): ItemBuilder {
        level = value
        return this
    }

    fun weight(value: Double): ItemBuilder {
        weight = value
        return this
    }

    fun material(value: Material): ItemBuilder {
        material = value
        return this
    }

    fun isContainer(value: Boolean): ItemBuilder {
        isContainer = value
        return this
    }

    fun canOwn(value: Boolean): ItemBuilder {
        canOwn = value
        return this
    }

    fun affects(value: List<Affect>): ItemBuilder {
        affects = value
        return this
    }

    fun canonicalId(value: ItemCanonicalId): ItemBuilder {
        canonicalId = value
        return this
    }

    fun position(value: Position): ItemBuilder {
        position = value
        return this
    }

    fun attackVerb(value: String): ItemBuilder {
        attackVerb = value
        return this
    }

    fun damageType(value: DamageType): ItemBuilder {
        damageType = value
        return this
    }

    fun drink(value: Drink): ItemBuilder {
        drink = value
        type = ItemType.DRINK
        return this
    }

    fun food(value: Food): ItemBuilder {
        food = value
        type = ItemType.FOOD
        return this
    }

    fun quantity(value: Int): ItemBuilder {
        quantity = value
        return this
    }

    fun decayTimer(value: Int): ItemBuilder {
        decayTimer = value
        return this
    }

    fun maxItems(value: Int): ItemBuilder {
        maxItems = value
        return this
    }

    fun maxWeight(value: Int): ItemBuilder {
        maxWeight = value
        return this
    }

    fun attributes(value: Map<Attribute, Int>): ItemBuilder {
        attributes = value
        return this
    }

    fun items(value: List<Item>): ItemBuilder {
        items = value
        return this
    }

    fun room(value: Room): ItemBuilder {
        room = value
        return this
    }

    fun build(): Item {
        val item = Item(
            type,
            name,
            description,
            worth,
            level,
            weight,
            material,
            isContainer,
            canOwn,
            affects.toMutableList(),
            canonicalId,
            position,
            attackVerb,
            damageType,
            drink,
            food,
            quantity,
            decayTimer,
            maxItems,
            maxWeight,
            attributes?.toMutableMap() ?: mutableMapOf(),
            items?.toMutableList(),
        )
        itemService.add(item)
        room?.items?.add(item)
        return item
    }
}

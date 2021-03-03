package kotlinmud.item.builder

import kotlinmud.affect.model.Affect
import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.type.Attribute
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.*
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.room.model.Room

class ItemBuilder(private val itemService: ItemService) {
    private lateinit var type: ItemType
    private lateinit var name: String
    private lateinit var description: String
    private var worth: Int = 1
    private var level: Int = 1
    private var weight: Double = 1.0
    private lateinit var material: Material
    private var isContainer = false
    private var canOwn = true
    private var affects: List<Affect> = listOf()
    private var canonicalId: ItemCanonicalId? = null
    private var position: Position? = null
    private var attackVerb: String? = null
    private var damageType: DamageType? = null
    private var drink: Drink? = null
    private var food: Food? = null
    private var quantity: Int? = null
    private var decayTimer: Int? = null
    private var maxItems: Int? = null
    private var maxWeight: Int? = null
    private var attributes: Map<Attribute, Int>? = null
    private var items: List<Item>? = null
    private var room: Room? = null

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

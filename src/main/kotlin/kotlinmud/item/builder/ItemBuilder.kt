package kotlinmud.item.builder

import kotlinmud.affect.model.Affect
import kotlinmud.attributes.type.Attribute
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.Drink
import kotlinmud.item.type.Food
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.item.type.Weapon
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.room.model.Room
import java.util.UUID

class ItemBuilder(private val itemService: ItemService) {
    lateinit var type: ItemType
    lateinit var name: String
    lateinit var description: String
    lateinit var material: Material
    lateinit var brief: String
    var id: Int = 0
    var worth: Int = 1
    var level: Int = 1
    var weight: Double = 1.0
    var isContainer = false
    var canOwn = true
    var affects: List<Affect> = listOf()
    var spells: List<SkillType> = listOf()
    var canonicalId: UUID = UUID.randomUUID()
    var position: Position? = null
    var weaponType: Weapon? = null
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

    fun makeContainer(maxItems: Int = 10, maxWeight: Int = 100): ItemBuilder {
        type = ItemType.CONTAINER
        isContainer = true
        items = listOf()
        this.maxItems = maxItems
        this.maxWeight = maxWeight

        return this
    }

    fun makeWeapon(
        weaponType: Weapon,
        damageType: DamageType,
        attackVerb: String,
        material: Material,
        hit: Int,
        dam: Int,
    ): ItemBuilder {
        type = ItemType.EQUIPMENT
        position = Position.WEAPON
        this.weaponType = weaponType
        this.damageType = damageType
        this.attackVerb = attackVerb
        this.material = material
        this.attributes = mapOf(
            Pair(Attribute.HIT, hit),
            Pair(Attribute.DAM, dam),
        )

        return this
    }

    fun makeOrgans(): ItemBuilder {
        type = ItemType.ORGANS
        material = Material.ORGANIC

        return this
    }

    fun build(): Item {
        if (!this::brief.isInitialized) {
            brief = "$name is here"
        }
        val item = Item(
            id,
            type,
            name,
            brief,
            description,
            worth,
            level,
            weight,
            material,
            isContainer,
            canOwn,
            affects.toMutableList(),
            spells.toMutableList(),
            canonicalId,
            position,
            weaponType,
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

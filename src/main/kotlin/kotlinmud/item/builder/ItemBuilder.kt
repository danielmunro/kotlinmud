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
import kotlinmud.type.Builder
import java.util.UUID

class ItemBuilder(private val itemService: ItemService) : Builder {
    lateinit var type: ItemType
    override lateinit var name: String
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
    var attributes: MutableMap<Attribute, Int> = mutableMapOf()
    var items: List<Item>? = null
    override var room: Room? = null

    override fun setFromKeyword(keyword: String, value: String) {
        when (keyword) {
            "food" -> {
                type = ItemType.FOOD
                material = Material.ORGANIC
                quantity = value.toInt()
            }
            "weight" -> {
                weight = value.toDouble()
            }
            "type" -> {
                type = ItemType.valueOf(value.toUpperCase())
                if (type == ItemType.FURNITURE) {
                    canOwn = false
                }
            }
            "position" -> {
                position = Position.valueOf(value.toUpperCase())
                type = ItemType.EQUIPMENT
            }
            "material" -> {
                material = Material.valueOf(value.toUpperCase())
            }
            "worth" -> {
                worth = value.toInt()
            }
            "verb" -> {
                attackVerb = value
            }
            "hit" -> {
                val amounts = value.split("d")
                attributes[Attribute.HIT] = amounts[0].toInt()
                attributes[Attribute.DAM] = amounts[1].toInt()
            }
            "damage" -> {
                damageType = DamageType.valueOf(value.toUpperCase())
            }
            "level" -> {
                level = value.toInt()
            }
            "weapon" -> {
                weaponType = Weapon.valueOf(value.toUpperCase())
            }
        }
    }

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
        this.attributes = mutableMapOf(
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

    override fun build(): Item {
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
            attributes,
            items?.toMutableList(),
        )
        itemService.add(item)
        room?.items?.add(item)
        return item
    }
}

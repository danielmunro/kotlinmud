package kotlinmud.mob

import kotlinmud.Noun
import kotlinmud.affect.AffectInstance
import kotlinmud.attributes.Attribute
import kotlinmud.attributes.Attributes
import kotlinmud.attributes.HasAttributes
import kotlinmud.data.Row
import kotlinmud.item.Inventory
import kotlinmud.item.Item
import kotlinmud.item.Material
import kotlinmud.item.Position
import kotlinmud.mob.fight.AttackType
import kotlinmud.mob.fight.DamageType
import kotlinmud.mob.race.Race
import kotlinmud.mob.skill.SkillType

const val corpseWeight = 20.0

class Mob(
    override val id: Int,
    override val name: String,
    var brief: String,
    override var description: String,
    var disposition: Disposition,
    var hp: Int,
    var mana: Int,
    var mv: Int,
    var level: Int,
    val race: Race,
    val specialization: SpecializationType,
    val attributes: Attributes,
    val job: JobType,
    var gender: Gender = Gender.NONE,
    var gold: Int = 0,
    val inventory: Inventory = Inventory(),
    val equipped: Inventory = Inventory(),
    var skills: Map<SkillType, Int> = mapOf(),
    val affects: MutableList<AffectInstance> = mutableListOf()
) : Noun, Row {
    var delay = 0

    fun isSleeping(): Boolean {
        return disposition == Disposition.SLEEPING
    }

    fun isIncapacitated(): Boolean {
        return disposition == Disposition.DEAD
    }

    fun isStanding(): Boolean {
        return disposition == Disposition.STANDING
    }

    fun getAttacks(): List<AttackType> {
        return arrayListOf(AttackType.FIRST)
    }

    fun getDamageType(): DamageType {
        return DamageType.POUND
    }

    fun calc(attribute: Attribute): Int {
        return when (attribute) {
            Attribute.HP -> attributes.hp + accumulate { it.attributes.hp }
            Attribute.MANA -> attributes.mana + accumulate { it.attributes.mana }
            Attribute.MV -> attributes.mv + accumulate { it.attributes.mv }
            Attribute.STR -> attributes.str +
                    race.attributes.str +
                    accumulate { it.attributes.str }
            Attribute.INT -> attributes.int +
                    race.attributes.int +
                    accumulate { it.attributes.int }
            Attribute.WIS -> attributes.wis +
                    race.attributes.wis +
                    accumulate { it.attributes.wis }
            Attribute.DEX -> attributes.dex +
                    race.attributes.dex +
                    accumulate { it.attributes.dex }
            Attribute.CON -> attributes.con +
                    race.attributes.con +
                    accumulate { it.attributes.con }
            Attribute.HIT -> attributes.hit + accumulate { it.attributes.hit }
            Attribute.DAM -> attributes.dam + accumulate { it.attributes.dam }
            Attribute.AC_BASH -> attributes.acBash + accumulate { it.attributes.acBash }
            Attribute.AC_PIERCE -> attributes.acPierce + accumulate { it.attributes.acPierce }
            Attribute.AC_SLASH -> attributes.acSlash + accumulate { it.attributes.acSlash }
            Attribute.AC_MAGIC -> attributes.acMagic + accumulate { it.attributes.acMagic }
        }
    }

    fun createCorpse(): Item {
        val corpse = Item(
            0,
            "a corpse of $name",
            "a corpse of $name is here.",
            0,
            level,
            corpseWeight,
            Attributes(),
            Material.ORGANIC,
            Position.NONE,
            Inventory()
        )
        inventory.items.forEach {
            inventory.items.remove(it)
            corpse.inventory?.items?.add(it)
        }
        return corpse
    }

    fun copy(): Mob {
        return Mob(
            id,
            name,
            brief,
            description,
            disposition,
            hp,
            mana,
            mv,
            level,
            race,
            specialization,
            attributes.copy(),
            job,
            gender,
            gold,
            inventory.copy(),
            equipped.copy(),
            skills.toMap(),
            affects.toMutableList()
        )
    }

    override fun toString(): String {
        return name
    }

    private fun accumulate(accumulator: (HasAttributes) -> Int): Int {
        return equipped.items.map(accumulator).fold(0) { acc: Int, it: Int -> acc + it } +
                affects.map(accumulator).fold(0) { acc: Int, it: Int -> acc + it }
    }
}

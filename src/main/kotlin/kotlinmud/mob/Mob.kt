package kotlinmud.mob

import io.github.serpro69.kfaker.Faker
import kotlinmud.Noun
import kotlinmud.affect.AffectInstance
import kotlinmud.attributes.*
import kotlinmud.data.Row
import kotlinmud.item.Inventory
import kotlinmud.item.Item
import kotlinmud.item.Material
import kotlinmud.item.Position
import kotlinmud.mob.fight.AttackType
import kotlinmud.mob.fight.DamageType
import kotlinmud.mob.race.Race
import kotlinmud.mob.race.impl.Human
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
    var trains = 0
    var practices = 0
    var skillPoints = 0
    var bounty = 0
    var experience = 0
    var wimpy = 0
    var sacPoints = 0

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

    fun getSaves(): Int {
        return 1
    }

    fun base(attribute: Attribute): Int {
        return when (attribute) {
            Attribute.STR -> attributes.str + race.attributes.str
            Attribute.INT -> attributes.int + race.attributes.int
            Attribute.WIS -> attributes.wis + race.attributes.wis
            Attribute.DEX -> attributes.dex + race.attributes.dex
            Attribute.CON -> attributes.con + race.attributes.con
            else -> 0
        }
    }

    fun calc(attribute: Attribute): Int {
        return when (attribute) {
            Attribute.HP -> attributes.hp + accumulate { it.attributes.hp }
            Attribute.MANA -> attributes.mana + accumulate { it.attributes.mana }
            Attribute.MV -> attributes.mv + accumulate { it.attributes.mv }
            Attribute.STR -> base(attribute) + accumulate { it.attributes.str }
            Attribute.INT -> base(attribute) + accumulate { it.attributes.int }
            Attribute.WIS -> base(attribute) + accumulate { it.attributes.wis }
            Attribute.DEX -> base(attribute) + accumulate { it.attributes.dex }
            Attribute.CON -> base(attribute) + accumulate { it.attributes.con }
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

    class Builder {
        var id = 0
        var name = Faker().name.name()
        var brief = ""
        var description = ""
        var disposition = Disposition.STANDING
        var hp = startingHp
        var mana = startingMana
        var mv = startingMv
        var level = 1
        var race = Human()
        var specialization = SpecializationType.NONE
        var attributes = Attributes()
        var job = JobType.NONE
        var gender = Gender.NONE
        var gold = 0
        var inventory: Inventory = Inventory()
        var equipped: Inventory = Inventory()
        var skills: Map<SkillType, Int> = mapOf()
        var affects: MutableList<AffectInstance> = mutableListOf()
        var delay = 0
        var trains = 0
        var practices = 0
        var skillPoints = 0
        var bounty = 0
        var experience = 0
        var wimpy = 0
        var sacPoints = 0

        fun setId(value: Int): Builder {
            id = value
            return this
        }

        fun setName(value: String): Builder {
            name = value
            return this
        }

        fun setBrief(value: String): Builder {
            brief = value
            return this
        }

        fun setDescription(value: String): Builder {
            description = value
            return this
        }

        fun build(): Mob {
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
                attributes,
                job,
                gender,
                gold,
                inventory,
                equipped,
                skills,
                affects
            )
        }
    }
}

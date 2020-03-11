package kotlinmud.mob

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
    var gender: Gender,
    var gold: Int,
    var skills: Map<SkillType, Int>,
    val affects: MutableList<AffectInstance>,
    var wimpy: Int,
    var experiencePerLevel: Int,
    var savingThrows: Int
) : Noun, Row {
    val inventory: Inventory = Inventory()
    val equipped: Inventory = Inventory()
    var delay = 0
    var trains = 0
    var practices = 0
    var skillPoints = 0
    var bounty = 0
    var experience = 0
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

    fun addExperience(value: Int): AddExperience {
        experience += value
        var didLevel = false
        val toLevel = getExperienceToLevel()
        if (toLevel < 0) {
            level++
            didLevel = true
        }
        return AddExperience(experience, didLevel)
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
            skills.toMap(),
            affects.toMutableList(),
            wimpy,
            experiencePerLevel,
            savingThrows
        )
    }

    override fun toString(): String {
        return name
    }

    private fun getExperienceToLevel(): Int {
        return experiencePerLevel - experience + (experiencePerLevel * level)
    }

    private fun accumulate(accumulator: (HasAttributes) -> Int): Int {
        return equipped.items.map(accumulator).fold(0) { acc: Int, it: Int -> acc + it } +
                affects.map(accumulator).fold(0) { acc: Int, it: Int -> acc + it }
    }

    class Builder(val id: Int, val name: String) {
        var brief = "$name is here."
        var description = "$name is here, minding their own business."
        var disposition = Disposition.STANDING
        var hp = startingHp
        var mana = startingMana
        var mv = startingMv
        var level = 1
        var race: Race = Human()
        var specialization = SpecializationType.NONE
        var job = JobType.NONE
        var gender = Gender.NONE
        var gold = 0
        var skills: Map<SkillType, Int> = mapOf()
        var affects: MutableList<AffectInstance> = mutableListOf()
        var wimpy = 0
        var experiencePerLevel = 1000
        var savingThrows = 0

        fun setBrief(value: String): Builder {
            brief = value
            return this
        }

        fun setDescription(value: String): Builder {
            description = value
            return this
        }

        fun setDisposition(value: Disposition): Builder {
            disposition = value
            return this
        }

        fun setHp(value: Int): Builder {
            hp = value
            return this
        }

        fun setMana(value: Int): Builder {
            mana = value
            return this
        }

        fun setMv(value: Int): Builder {
            mv = value
            return this
        }

        fun setLevel(value: Int): Builder {
            level = value
            return this
        }

        fun setRace(value: Race): Builder {
            race = value
            return this
        }

        fun setSpecialization(value: SpecializationType): Builder {
            specialization = value
            return this
        }

        fun setJob(value: JobType): Builder {
            job = value
            return this
        }

        fun setGender(value: Gender): Builder {
            gender = value
            return this
        }

        fun setGold(value: Int): Builder {
            gold = value
            return this
        }

        fun setWimpy(value: Int): Builder {
            wimpy = value
            return this
        }

        fun setExperiencePerLevel(value: Int): Builder {
            experiencePerLevel = value
            return this
        }

        fun setSavingThrows(value: Int): Builder {
            savingThrows = value
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
                Attributes(
                    hp,
                    mana,
                    mv
                ),
                job,
                gender,
                gold,
                skills,
                affects,
                wimpy,
                experiencePerLevel,
                savingThrows
            )
        }
    }
}

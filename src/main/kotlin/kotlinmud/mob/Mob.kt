package kotlinmud.mob

import kotlinmud.Noun
import kotlinmud.affect.AffectInstance
import kotlinmud.affect.AffectType
import kotlinmud.attributes.Attribute
import kotlinmud.attributes.Attributes
import kotlinmud.attributes.HasAttributes
import kotlinmud.attributes.startingHp
import kotlinmud.attributes.startingMana
import kotlinmud.attributes.startingMv
import kotlinmud.data.Row
import kotlinmud.item.Inventory
import kotlinmud.item.Item
import kotlinmud.item.Material
import kotlinmud.item.Position
import kotlinmud.loader.model.Model
import kotlinmud.math.normalize
import kotlinmud.math.random.percentRoll
import kotlinmud.mob.fight.AttackType
import kotlinmud.mob.fight.DamageType
import kotlinmud.mob.race.Race
import kotlinmud.mob.race.RaceType
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.skill.SkillType

const val corpseWeight = 20.0
const val baseStat = 15

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
    val skills: Map<SkillType, Int>,
    override val affects: MutableList<AffectInstance>,
    var wimpy: Int,
    val experiencePerLevel: Int,
    var savingThrows: Int,
    val inventory: Inventory,
    val equipped: Inventory
) : Noun, Row {
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

    fun isAffectedBy(affectType: AffectType): Boolean {
        return affects.find { it.affectType == affectType } != null
    }

    fun base(attribute: Attribute): Int {
        return when (attribute) {
            Attribute.STR -> baseStat + attributes.str + race.attributes.str
            Attribute.INT -> baseStat + attributes.int + race.attributes.int
            Attribute.WIS -> baseStat + attributes.wis + race.attributes.wis
            Attribute.DEX -> baseStat + attributes.dex + race.attributes.dex
            Attribute.CON -> baseStat + attributes.con + race.attributes.con
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
            mutableListOf(),
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
            savingThrows,
            Inventory(),
            Inventory()
        )
    }

    fun savesAgainst(damageType: DamageType): Boolean {
        var saves = savingThrows

        if (race.type == RaceType.ELF) {
            saves -= 5
        }

        var base = 80 + (level / 6) + saves - calc(Attribute.WIS) - calc(Attribute.INT)

        if (isAffectedBy(AffectType.CURSE)) {
            base += 5
        }

        if (disposition == Disposition.FIGHTING) {
            base += 5
        }

        affects.find { it.affectType == AffectType.BERSERK }?.let {
            base -= level / 10
        }

        base += when {
            race.vulnerableTo.contains(damageType) -> 25
            race.resist.contains(damageType) -> -25
            race.immuneTo.contains(damageType) -> return true
            else -> 0
        }

        if (specialization == SpecializationType.MAGE) {
            base -= 5
        } else if (specialization == SpecializationType.CLERIC) {
            base -= 3
        }

        return percentRoll() > normalize(5, base, 95)
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

    class Builder(override val id: Int, val name: String) : Model {
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
        var skills: MutableMap<SkillType, Int> = mutableMapOf()
        var affects: MutableList<AffectInstance> = mutableListOf()
        var wimpy = 0
        var experiencePerLevel = 1000
        var savingThrows = 0
        var inventory = Inventory()
        var equipment = Inventory()

        fun addSkill(skillType: SkillType, level: Int): Builder {
            skills[skillType] = level
            return this
        }

        fun addAffect(affect: AffectType): Builder {
            affects.add(AffectInstance(affect, 1))
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

        fun equip(item: Item): Builder {
            // @todo check for equipment position exception
            equipment.items.add(item)
            return this
        }

        fun addItem(item: Item): Builder {
            inventory.items.add(item)
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
                savingThrows,
                inventory,
                equipment
            )
        }
    }
}

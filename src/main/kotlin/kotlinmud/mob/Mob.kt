package kotlinmud.mob

import com.thinkinglogic.builder.annotation.Builder
import com.thinkinglogic.builder.annotation.DefaultValue
import com.thinkinglogic.builder.annotation.Mutable
import java.util.stream.Collectors
import kotlinmud.Noun
import kotlinmud.affect.AffectInstance
import kotlinmud.affect.AffectType
import kotlinmud.attributes.Attribute
import kotlinmud.attributes.Attributes
import kotlinmud.attributes.HasAttributes
import kotlinmud.data.Row
import kotlinmud.item.HasInventory
import kotlinmud.item.Inventory
import kotlinmud.item.Item
import kotlinmud.item.ItemBuilder
import kotlinmud.item.Position
import kotlinmud.math.dN
import kotlinmud.math.normalizeInt
import kotlinmud.math.percentRoll
import kotlinmud.mob.fight.AttackType
import kotlinmud.mob.fight.DamageType
import kotlinmud.mob.race.Race
import kotlinmud.mob.race.RaceType
import kotlinmud.mob.skill.SkillType
import kotlinmud.service.AffectService

const val corpseWeight = 20.0
const val baseStat = 15

@Builder
class Mob(
    override val id: Int,
    override val name: String,
    @DefaultValue("a nondescript mob is here") var brief: String,
    @DefaultValue("a nondescript mob is standing here.") override var description: String,
    var disposition: Disposition,
    var hp: Int,
    var mana: Int,
    var mv: Int,
    var level: Int,
    val race: Race,
    val specialization: SpecializationType,
    val attributes: Attributes,
    @Mutable val trainedAttributes: MutableList<Attributes>,
    val job: JobType,
    var gender: Gender,
    var gold: Int,
    @Mutable val skills: MutableMap<SkillType, Int>,
    @Mutable override val affects: MutableList<AffectInstance>,
    var wimpy: Int,
    val experiencePerLevel: Int,
    var savingThrows: Int,
    val inventory: Inventory,
    val equipped: Inventory,
    val isNpc: Boolean,
    var trains: Int,
    var practices: Int,
    val route: List<Int>
) : Noun, Row, HasInventory {
    var delay = 0
    var skillPoints = 0
    var bounty = 0
    var experience = 0
    var sacPoints = 0
    var lastRoute = 0
    val appetite: Appetite = Appetite(race)

    override fun affects(): AffectService {
        return AffectService(this)
    }

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

    fun getAttackVerb(): String {
        return equipped.items.find {
                it.position == Position.WEAPON
            }?.attackVerb ?: race.unarmedAttackVerb
    }

    fun base(attribute: Attribute): Int {
        return when (attribute) {
            Attribute.STR -> baseStat +
                    attributes.strength +
                    race.attributes.strength +
                    calcTrained(Attribute.STR)
            Attribute.INT -> baseStat +
                    attributes.intelligence +
                    race.attributes.intelligence +
                    calcTrained(Attribute.INT)
            Attribute.WIS -> baseStat +
                    attributes.wisdom +
                    race.attributes.wisdom +
                    calcTrained(Attribute.WIS)
            Attribute.DEX -> baseStat +
                    attributes.dexterity +
                    race.attributes.dexterity +
                    calcTrained(Attribute.DEX)
            Attribute.CON -> baseStat +
                    attributes.constitution +
                    race.attributes.constitution +
                    calcTrained(Attribute.CON)
            else -> 0
        }
    }

    fun calc(attribute: Attribute): Int {
        return when (attribute) {
            Attribute.HP -> attributes.hp +
                    accumulate { it.attributes.hp } +
                    calcTrained(Attribute.HP)
            Attribute.MANA -> attributes.mana +
                    accumulate { it.attributes.mana } +
                    calcTrained(Attribute.MANA)
            Attribute.MV -> attributes.mv +
                    accumulate { it.attributes.mv } +
                    calcTrained(Attribute.MV)
            Attribute.STR -> base(attribute) + accumulate { it.attributes.strength }
            Attribute.INT -> base(attribute) + accumulate { it.attributes.intelligence }
            Attribute.WIS -> base(attribute) + accumulate { it.attributes.wisdom }
            Attribute.DEX -> base(attribute) + accumulate { it.attributes.dexterity }
            Attribute.CON -> base(attribute) + accumulate { it.attributes.constitution }
            Attribute.HIT -> attributes.hit + accumulate { it.attributes.hit }
            Attribute.DAM -> attributes.dam + accumulate { it.attributes.dam }
            Attribute.AC_BASH -> attributes.acBash + accumulate { it.attributes.acBash }
            Attribute.AC_PIERCE -> attributes.acPierce + accumulate { it.attributes.acPierce }
            Attribute.AC_SLASH -> attributes.acSlash + accumulate { it.attributes.acSlash }
            Attribute.AC_MAGIC -> attributes.acMagic + accumulate { it.attributes.acMagic }
        }
    }

    fun createCorpse(): Item {
        val corpse = ItemBuilder()
            .id(0)
            .name("a corpse of $name")
            .description("a corpse of $name is here.")
            .level(level)
            .weight(corpseWeight)
            .inventory(Inventory())
            .decayTimer(20)
            .build()

        equipped.items.stream().collect(Collectors.toList()).forEach {
            equipped.items.remove(it)
            corpse.inventory?.items?.add(it)
        }

        inventory.items.stream().collect(Collectors.toList()).forEach {
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
            trainedAttributes.toMutableList(),
            job,
            gender,
            gold,
            skills.toMutableMap(),
            affects.toMutableList(),
            wimpy,
            experiencePerLevel,
            savingThrows,
            Inventory(),
            Inventory(),
            isNpc,
            trains,
            practices,
            route
        )
    }

    fun savesAgainst(damageType: DamageType): Boolean {
        var saves = savingThrows

        if (race.type == RaceType.ELF) {
            saves -= 5
        }

        var base = 80 + (level / 6) + saves - calc(Attribute.WIS) - calc(Attribute.INT)

        if (affects().findByType(AffectType.CURSE) != null) {
            base += 5
        }

        if (disposition == Disposition.FIGHTING) {
            base += 5
        }

        affects().findByType(AffectType.BERSERK)?.let {
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

        return percentRoll() > normalizeInt(5, base, 95)
    }

    fun increaseHp(value: Int) {
        hp += value
        with(calc(Attribute.HP)) {
            if (hp > this) {
                hp = this
            }
        }
    }

    fun increaseMana(value: Int) {
        mana += value
        with(calc(Attribute.MANA)) {
            if (mana > this) {
                mana = this
            }
        }
    }

    fun increaseMv(value: Int) {
        mv += value
        with(calc(Attribute.MV)) {
            if (mv > this) {
                mv = this
            }
        }
    }

    override fun toString(): String {
        return name
    }

    fun calcTrained(attribute: Attribute): Int {
        return trainedAttributes.fold(0) { acc, it -> acc + it.getAttribute(attribute) }
    }

    fun wantsToMove(): Boolean {
        if (!isNpc) {
            return false
        }

        if (job != JobType.SCAVENGER &&
            job != JobType.FODDER &&
            job != JobType.PATROL) {
            return false
        }

        return dN(1, 2) == 1
    }

    private fun getExperienceToLevel(): Int {
        return experiencePerLevel - experience + (experiencePerLevel * level)
    }

    private fun accumulate(accumulator: (HasAttributes) -> Int): Int {
        return equipped.items.map(accumulator).fold(0) { acc: Int, it: Int -> acc + it } +
                affects.map(accumulator).fold(0) { acc: Int, it: Int -> acc + it }
    }
}

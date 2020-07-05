package kotlinmud.mob.model

import com.thinkinglogic.builder.annotation.Builder
import com.thinkinglogic.builder.annotation.DefaultValue
import com.thinkinglogic.builder.annotation.Mutable
import kotlinmud.affect.model.AffectInstance
import kotlinmud.affect.service.AffectService
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.model.Attributes
import kotlinmud.attributes.type.Attribute
import kotlinmud.attributes.type.HasAttributes
import kotlinmud.data.Row
import kotlinmud.helper.Noun
import kotlinmud.helper.math.dN
import kotlinmud.helper.math.normalizeInt
import kotlinmud.helper.math.percentRoll
import kotlinmud.item.model.Item
import kotlinmud.item.type.HasInventory
import kotlinmud.item.type.Position
import kotlinmud.mob.fight.AttackType
import kotlinmud.mob.fight.DamageType
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.Gender
import kotlinmud.mob.type.JobType
import kotlinmud.mob.type.Rarity
import kotlinmud.mob.type.SpecializationType

const val corpseWeight = 20.0
const val baseStat = 15
const val MAX_WALKABLE_ELEVATION = 2

@Builder
class Mob(
    override val id: Int,
    override val name: String,
    @DefaultValue("a nondescript mob is here") var brief: String,
    @DefaultValue("a nondescript mob is standing here.") override var description: String,
    @DefaultValue("Disposition.STANDING") var disposition: Disposition,
    var hp: Int,
    var mana: Int,
    var mv: Int,
    @DefaultValue("1") var level: Int,
    val race: Race,
    @DefaultValue("SpecializationType.NONE") val specialization: SpecializationType,
    @DefaultValue("Attributes()") val attributes: Attributes,
    @DefaultValue("JobType.NONE") val job: JobType,
    @DefaultValue("Gender.NONE") var gender: Gender,
    @DefaultValue("0") var gold: Int,
    @DefaultValue("0") var goldMin: Int,
    @DefaultValue("0") var goldMax: Int,
    @DefaultValue("mutableMapOf()") @Mutable val skills: MutableMap<SkillType, Int>,
    @DefaultValue("mutableListOf()") @Mutable val affects: MutableList<AffectInstance>,
    @DefaultValue("0") var wimpy: Int,
    @DefaultValue("0") var savingThrows: Int,
    @DefaultValue("mutableListOf()") @Mutable val equipped: MutableList<Item>,
    @DefaultValue("true") val isNpc: Boolean,
    @DefaultValue("listOf()") val route: List<Int>,
    @DefaultValue("50") val maxItems: Int,
    @DefaultValue("100") val maxWeight: Int,
    @DefaultValue("Rarity.COMMON") val rarity: Rarity
) : Noun, Row {
    var lastRoute = 0

    fun affects(): AffectService {
        return AffectService(this)
    }

    fun isStanding(): Boolean {
        return disposition == Disposition.STANDING
    }

    fun base(attribute: Attribute): Int {
        return when (attribute) {
            Attribute.STR -> baseStat +
                    attributes.strength +
                    race.attributes.strength
            Attribute.INT -> baseStat +
                    attributes.intelligence +
                    race.attributes.intelligence
            Attribute.WIS -> baseStat +
                    attributes.wisdom +
                    race.attributes.wisdom
            Attribute.DEX -> baseStat +
                    attributes.dexterity +
                    race.attributes.dexterity
            Attribute.CON -> baseStat +
                    attributes.constitution +
                    race.attributes.constitution
            else -> 0
        }
    }

    fun calc(attribute: Attribute): Int {
        return when (attribute) {
            Attribute.HP -> attributes.hp +
                    accumulate { it.attributes.hp }
            Attribute.MANA -> attributes.mana +
                    accumulate { it.attributes.mana }
            Attribute.MV -> attributes.mv +
                    accumulate { it.attributes.mv }
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

    fun savesAgainst(damageType: DamageType): Boolean {
        var saves = savingThrows

        if (race.type == RaceType.ELF) {
            saves -= 5
        }

        var base = 80 + (level / 6) + saves - calc(Attribute.WIS) - calc(
            Attribute.INT)

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

    override fun toString(): String {
        return name
    }

    private fun accumulate(accumulator: (HasAttributes) -> Int): Int {
        return equipped.map(accumulator).fold(0) { acc: Int, it: Int -> acc + it } +
                affects.map(accumulator).fold(0) { acc: Int, it: Int -> acc + it }
    }
}

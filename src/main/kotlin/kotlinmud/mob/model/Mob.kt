package kotlinmud.mob.model

import kotlinmud.affect.dao.AffectDAO
import kotlinmud.affect.model.AttributeAffect
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.type.Attribute
import kotlinmud.attributes.type.HasAttributes
import kotlinmud.helper.Noun
import kotlinmud.helper.math.dice
import kotlinmud.helper.math.normalizeInt
import kotlinmud.helper.math.percentRoll
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.HasInventory
import kotlinmud.item.type.Position
import kotlinmud.mob.constant.BASE_STAT
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.helper.getSkillBoostRegenRate
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.skill.dao.SkillDAO
import kotlinmud.mob.skill.model.Skill
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.specialization.type.Specialization
import kotlinmud.mob.specialization.type.SpecializationType
import kotlinmud.mob.type.CurrencyType
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.Gender
import kotlinmud.mob.type.JobType
import kotlinmud.mob.type.MobCanonicalId
import kotlinmud.mob.type.Rarity
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.room.dao.RoomDAO
import org.jetbrains.exposed.sql.transactions.transaction

class Mob(
    override val name: String,
    val brief: String,
    override val description: String,
    var hp: Int,
    var mana: Int,
    var mv: Int,
    var level: Int,
    val race: Race,
    var disposition: Disposition,
    val job: JobType,
    val specialization: Specialization?,
    val gender: Gender,
    var wimpy: Int,
    val savingThrows: Int,
    val rarity: Rarity,
    val canonicalId: MobCanonicalId?,
    val attributes: AttributesDAO,
    var room: RoomDAO,
    val equipped: MutableList<ItemDAO>,
    override val maxItems: Int,
    override val maxWeight: Int,
    override val items: MutableList<ItemDAO>,
    val skills: MutableList<Skill>,
    val affects: MutableList<AffectDAO>,
    val currencies: MutableMap<CurrencyType, Int>,
    val mobCard: MobCardDAO?
) : Noun, HasInventory {
    fun getHealthIndication(): String {
        val amount: Double = hp.toDouble() / calc(Attribute.HP).toDouble()
        return when {
            amount == 1.0 -> "$name is in excellent condition."
            amount > 0.9 -> "$name has a few scratches."
            amount > 0.75 -> "$name has some small wounds and bruises."
            amount > 0.5 -> "$name has quite a few wounds."
            amount > 0.3 -> "$name has some big nasty wounds and scratches."
            amount > 0.15 -> "$name looks pretty hurt."
            else -> "$name is in awful condition."
        }
    }

    fun getDamageType(): DamageType {
        return getWeapon()?.damageType ?: race.unarmedDamageType
    }

    fun getAttackVerb(): String {
        return getWeapon()?.attackVerb ?: race.unarmedAttackVerb
    }

    fun calculateDamage(): Int {
        val hit = calc(Attribute.HIT)
        val dam = calc(Attribute.DAM)

        return dice(hit, dam) + dam
    }

    fun getEquippedByPosition(position: Position): ItemDAO? {
        return equipped.find { it.position == position }
    }

    fun getSkill(skillType: SkillType): Skill? {
        return skills.find { it.type == skillType }
    }

    fun getCurrency(currencyType: CurrencyType): Int {
        return currencies.getOrDefault(currencyType, 0)
    }

    fun addCurrency(currencyType: CurrencyType, amount: Int) {
        currencies[currencyType]?.let {
            currencies[currencyType] = it + amount
        } ?: run {
            currencies[currencyType] = amount
        }
    }

    fun canTargetForFight(): Boolean {
        return job == JobType.AGGRESSIVE ||
            job == JobType.FODDER ||
            job == JobType.GUARD ||
            job == JobType.PATROL ||
            job == JobType.SCAVENGER ||
            job == JobType.NONE
    }

    fun increaseByRegenRate(hpRate: Double, manaRate: Double, mvRate: Double) {
        increaseHp(((hpRate + getSkillBoostRegenRate(this, Attribute.HP)) * calc(Attribute.HP)).toInt())
        increaseMana(((manaRate + getSkillBoostRegenRate(this, Attribute.MANA)) * calc(Attribute.MANA)).toInt())
        increaseMv((mvRate * calc(Attribute.MV)).toInt())
    }

    fun calc(attribute: Attribute): Int {
        return transaction {
            when (attribute) {
                Attribute.HP ->
                    attributes.hp +
                        accumulate { it.attributes?.hp ?: 0 } + (mobCard?.calcTrained(attribute) ?: 0)
                Attribute.MANA ->
                    attributes.mana +
                        accumulate { it.attributes?.mana ?: 0 } + (mobCard?.calcTrained(attribute) ?: 0)
                Attribute.MV ->
                    attributes.mv +
                        accumulate { it.attributes?.mv ?: 0 } + (mobCard?.calcTrained(attribute) ?: 0)
                Attribute.STR ->
                    base(attribute) +
                        accumulate { it.attributes?.strength ?: 0 } +
                        (mobCard?.calcTrained(attribute) ?: 0)
                Attribute.INT ->
                    base(attribute) +
                        accumulate { it.attributes?.intelligence ?: 0 } +
                        (mobCard?.calcTrained(attribute) ?: 0)
                Attribute.WIS ->
                    base(attribute) +
                        accumulate { it.attributes?.wisdom ?: 0 } +
                        (mobCard?.calcTrained(attribute) ?: 0)
                Attribute.DEX ->
                    base(attribute) +
                        accumulate { it.attributes?.dexterity ?: 0 } +
                        (mobCard?.calcTrained(attribute) ?: 0)
                Attribute.CON ->
                    base(attribute) +
                        accumulate { it.attributes?.constitution ?: 0 } +
                        (mobCard?.calcTrained(attribute) ?: 0)
                Attribute.HIT -> attributes.hit + accumulate { it.attributes?.hit ?: 0 }
                Attribute.DAM -> attributes.dam + accumulate { it.attributes?.dam ?: 0 }
                Attribute.AC_BASH -> attributes.acBash + accumulate { it.attributes?.acBash ?: 0 }
                Attribute.AC_PIERCE -> attributes.acPierce + accumulate { it.attributes?.acPierce ?: 0 }
                Attribute.AC_SLASH -> attributes.acSlash + accumulate { it.attributes?.acSlash ?: 0 }
                Attribute.AC_MAGIC -> attributes.acMagic + accumulate { it.attributes?.acMagic ?: 0 }
            }
        }
    }

    fun base(attribute: Attribute): Int {
        return when (attribute) {
            Attribute.STR ->
                BASE_STAT +
                    attributes.strength +
                    race.attributes.strength
            Attribute.INT ->
                BASE_STAT +
                    attributes.intelligence +
                    race.attributes.intelligence
            Attribute.WIS ->
                BASE_STAT +
                    attributes.wisdom +
                    race.attributes.wisdom
            Attribute.DEX ->
                BASE_STAT +
                    attributes.dexterity +
                    race.attributes.dexterity
            Attribute.CON ->
                BASE_STAT +
                    attributes.constitution +
                    race.attributes.constitution
            else -> 0
        }
    }

    fun savesAgainst(damageType: DamageType): Boolean {
        return transaction {
            var saves = savingThrows

            if (race.type == RaceType.ELF) {
                saves -= 5
            }

            var base = 80 + (level / 6) + saves - calc(Attribute.WIS) - calc(
                Attribute.INT
            )

            if (affects.find { it.type == AffectType.CURSE } != null) {
                base += 5
            }

            if (disposition == Disposition.FIGHTING) {
                base += 5
            }

            affects.find { it.type == AffectType.BERSERK }?.let {
                base -= level / 8
            }

            base += when {
                race.vulnerableTo.contains(damageType) -> 25
                race.resist.contains(damageType) -> -25
                race.immuneTo.contains(damageType) -> return@transaction true
                else -> 0
            }

            if (specialization?.type == SpecializationType.MAGE) {
                base -= 5
            } else if (specialization?.type == SpecializationType.CLERIC) {
                base -= 3
            }

            percentRoll() > normalizeInt(5, base, 95)
        }
    }

    fun increaseHp(value: Int) {
        hp += value
        with(calc(Attribute.HP)) {
            if (hp > this) {
                hp = this
            }
        }
    }

    private fun increaseMana(value: Int) {
        mana += value
        with(calc(Attribute.MANA)) {
            if (mana > this) {
                mana = this
            }
        }
    }

    private fun increaseMv(value: Int) {
        mv += value
        with(calc(Attribute.MV)) {
            if (mv > this) {
                mv = this
            }
        }
    }

    private fun getWeapon(): ItemDAO? {
        return equipped.find { it.position == Position.WEAPON }
    }

    private fun accumulate(accumulator: (HasAttributes) -> Int): Int {
        return equipped.map(accumulator).fold(0) { acc: Int, it: Int -> acc + it } +
            affects.asSequence().toList()
                .filter { it.attributes != null }
                .map { AttributeAffect(it) }
                .map(accumulator)
                .fold(0) { acc: Int, it: Int -> acc + it }
    }
}

package kotlinmud.mob.dao

import kotlinmud.affect.dao.AffectDAO
import kotlinmud.affect.model.AttributeAffect
import kotlinmud.affect.table.Affects
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.type.Attribute
import kotlinmud.attributes.type.HasAttributes
import kotlinmud.helper.Noun
import kotlinmud.helper.math.normalizeInt
import kotlinmud.helper.math.percentRoll
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.table.Items
import kotlinmud.item.type.HasInventory
import kotlinmud.item.type.Position
import kotlinmud.mob.constant.BASE_STAT
import kotlinmud.mob.fight.type.AttackType
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.factory.createRaceFromString
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.skill.dao.SkillDAO
import kotlinmud.mob.skill.table.Skills
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.specialization.type.SpecializationType
import kotlinmud.mob.table.Mobs
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.Gender
import kotlinmud.mob.type.JobType
import kotlinmud.mob.type.Rarity
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.player.dao.PlayerDAO
import kotlinmud.room.dao.RoomDAO
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.sql.transactions.transaction

class MobDAO(id: EntityID<Int>) : IntEntity(id), Noun, HasInventory {
    companion object : IntEntityClass<MobDAO>(Mobs)

    override var name by Mobs.name
    var brief by Mobs.brief
    override var description by Mobs.description
    var hp by Mobs.hp
    var mana by Mobs.mana
    var mv by Mobs.mv
    var level by Mobs.level
    var race by Mobs.race.transform(
        { it.type.toString() },
        { createRaceFromString(it) }
    )
    var specialization by Mobs.specialization.transform(
        { it.toString() },
        { it?.let { SpecializationType.valueOf(it) } }
    )
    var disposition by Mobs.disposition.transform(
        { it.toString() },
        { Disposition.valueOf(it) }
    )
    var job by Mobs.job.transform(
        { it.toString() },
        { it?.let { JobType.valueOf(it) } }
    )
    var gender by Mobs.gender.transform(
        { it.toString() },
        { it?.let { Gender.valueOf(it) } }
    )
    var gold by Mobs.gold
    var goldMin by Mobs.goldMin
    var goldMax by Mobs.goldMax
    var wimpy by Mobs.wimpy
    var savingThrows by Mobs.savingThrows
    var isNpc by Mobs.isNpc
    var route by Mobs.route.transform(
        { it?.joinToString(", ") },
        { it?.split(", ")?.map { value -> value.toInt() } }
    )
    var lastRoute by Mobs.lastRoute
    override var maxItems by Mobs.maxItems
    override var maxWeight by Mobs.maxWeight
    var rarity by Mobs.rarity.transform(
        { it.toString() },
        { Rarity.valueOf(it) }
    )
    var attributes by AttributesDAO referencedOn Mobs.attributesId
    var room by RoomDAO referencedOn Mobs.roomId
    val equipped by ItemDAO optionalReferrersOn Items.mobEquippedId
    override val items by ItemDAO optionalReferrersOn Items.mobInventoryId
    val skills by SkillDAO referrersOn Skills.mobId
    override val affects by AffectDAO optionalReferrersOn Affects.mobId
    var player by PlayerDAO optionalReferencedOn Mobs.playerId
    var mobCard by MobCardDAO optionalReferencedOn Mobs.mobCardId

    fun isStanding(): Boolean {
        return disposition == Disposition.STANDING
    }

    fun isIncapacitated(): Boolean {
        return disposition == Disposition.DEAD
    }

    fun isSleeping(): Boolean {
        return disposition == Disposition.SLEEPING
    }

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

    fun increaseByRegenRate(rate: Double) {
        increaseHp((rate * calc(Attribute.HP)).toInt())
        increaseMana((rate * calc(Attribute.MANA)).toInt())
        increaseMv((rate * calc(Attribute.MV)).toInt())
    }

    fun getAttacks(): List<AttackType> {
        return listOf(AttackType.FIRST)
    }

    fun calc(attribute: Attribute): Int {
        return transaction {
            when (attribute) {
                Attribute.HP -> attributes.hp +
                        accumulate { it.attributes?.hp ?: 0 } + (mobCard?.calcTrained(attribute) ?: 0)
                Attribute.MANA -> attributes.mana +
                        accumulate { it.attributes?.mana ?: 0 } + (mobCard?.calcTrained(attribute) ?: 0)
                Attribute.MV -> attributes.mv +
                        accumulate { it.attributes?.mv ?: 0 } + (mobCard?.calcTrained(attribute) ?: 0)
                Attribute.STR -> base(attribute) +
                        accumulate { it.attributes?.strength ?: 0 } +
                        (mobCard?.calcTrained(attribute) ?: 0)
                Attribute.INT -> base(attribute) +
                        accumulate { it.attributes?.intelligence ?: 0 } +
                        (mobCard?.calcTrained(attribute) ?: 0)
                Attribute.WIS -> base(attribute) +
                        accumulate { it.attributes?.wisdom ?: 0 } +
                        (mobCard?.calcTrained(attribute) ?: 0)
                Attribute.DEX -> base(attribute) +
                        accumulate { it.attributes?.dexterity ?: 0 } +
                        (mobCard?.calcTrained(attribute) ?: 0)
                Attribute.CON -> base(attribute) +
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

    fun getDamageType(): DamageType {
        return getWeapon()?.damageType ?: race.unarmedDamageType
    }

    fun getAttackVerb(): String {
        return getWeapon()?.attackVerb ?: race.unarmedAttackVerb
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

            if (specialization == SpecializationType.MAGE) {
                base -= 5
            } else if (specialization == SpecializationType.CLERIC) {
                base -= 3
            }

            percentRoll() > normalizeInt(5, base, 95)
        }
    }

    fun base(attribute: Attribute): Int {
        return when (attribute) {
            Attribute.STR -> BASE_STAT +
                    attributes.strength +
                    race.attributes.strength
            Attribute.INT -> BASE_STAT +
                    attributes.intelligence +
                    race.attributes.intelligence
            Attribute.WIS -> BASE_STAT +
                    attributes.wisdom +
                    race.attributes.wisdom
            Attribute.DEX -> BASE_STAT +
                    attributes.dexterity +
                    race.attributes.dexterity
            Attribute.CON -> BASE_STAT +
                    attributes.constitution +
                    race.attributes.constitution
            else -> 0
        }
    }

    fun getEquippedByPosition(position: Position): ItemDAO? {
        return transaction { equipped.find { it.position == position } }
    }

    fun getSkill(skillType: SkillType): SkillDAO? {
        return transaction { skills.find { it.type == skillType } }
    }

    fun isWimpyMode(): Boolean {
        return wimpy > hp && room.getAllExits().isNotEmpty()
    }

    fun increaseHp(value: Int) {
        transaction {
            hp += value
            with(calc(Attribute.HP)) {
                if (hp > this) {
                    hp = this
                }
            }
        }
    }

    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        return if (other is MobDAO) other.id.value == id.value else super.equals(other)
    }

    private fun getWeapon(): ItemDAO? {
        return transaction { equipped.find { it.position == Position.WEAPON } }
    }

    private fun accumulate(accumulator: (HasAttributes) -> Int): Int {
        return equipped.map(accumulator).fold(0) { acc: Int, it: Int -> acc + it } +
                affects.asSequence().toList()
                    .filter { it.attributes != null }
                    .map { AttributeAffect(it) }
                    .map(accumulator)
                    .fold(0) { acc: Int, it: Int -> acc + it }
    }

    private fun increaseMana(value: Int) {
        transaction {
            mana += value
            with(calc(Attribute.MANA)) {
                if (mana > this) {
                    mana = this
                }
            }
        }
    }

    private fun increaseMv(value: Int) {
        transaction {
            mv += value
            with(calc(Attribute.MV)) {
                if (mv > this) {
                    mv = this
                }
            }
        }
    }
}

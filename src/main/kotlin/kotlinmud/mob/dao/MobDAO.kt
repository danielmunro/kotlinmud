package kotlinmud.mob.dao

import kotlinmud.affect.dao.AffectDAO
import kotlinmud.affect.table.Affects
import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.table.Items
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.skill.dao.SkillDAO
import kotlinmud.mob.skill.table.Skills
import kotlinmud.mob.specialization.type.SpecializationType
import kotlinmud.mob.table.Currencies
import kotlinmud.mob.table.Mobs
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.Gender
import kotlinmud.player.dao.PlayerDAO
import kotlinmud.quest.dao.QuestDAO
import kotlinmud.quest.table.Quests
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class MobDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MobDAO>(Mobs)

    var emailAddress by Mobs.emailAddress
    var name by Mobs.name
    var brief by Mobs.brief
    var description by Mobs.description
    var hp by Mobs.hp
    var mana by Mobs.mana
    var mv by Mobs.mv
    var level by Mobs.level
    var experience by Mobs.experience
    var experiencePerLevel by Mobs.experiencePerLevel
    var race by Mobs.race.transform(
        { it.toString() },
        { RaceType.valueOf(it) }
    )
    var specialization by Mobs.specialization.transform(
        { it.toString() },
        { SpecializationType.valueOf(it) }
    )
    var disposition by Mobs.disposition.transform(
        { it.toString() },
        { Disposition.valueOf(it) }
    )
    var gender by Mobs.gender.transform(
        { it.toString() },
        { Gender.valueOf(it) }
    )
    var wimpy by Mobs.wimpy
    var savingThrows by Mobs.savingThrows
    var route by Mobs.route.transform(
        { it?.joinToString(", ") },
        { it?.split(", ")?.map { value -> value.toInt() } }
    )
    var lastRoute by Mobs.lastRoute
    var maxItems by Mobs.maxItems
    var maxWeight by Mobs.maxWeight
    var trains by Mobs.trains
    var practices by Mobs.practices
    var bounty by Mobs.bounty
    var sacPoints by Mobs.sacPoints
    var hunger by Mobs.hunger
    var thirst by Mobs.thirst
    var skillPoints by Mobs.skillPoints
    var loggedIn by Mobs.loggedIn
    var roomId by Mobs.roomId
    var attributes by AttributesDAO referencedOn Mobs.attributesId
    val equipped by ItemDAO optionalReferrersOn Items.mobEquippedId
    val items by ItemDAO optionalReferrersOn Items.mobInventoryId
    val skills by SkillDAO referrersOn Skills.mobId
    val affects by AffectDAO optionalReferrersOn Affects.mobId
    var player by PlayerDAO optionalReferencedOn Mobs.playerId
    val currencies by CurrencyDAO referrersOn Currencies.mobId
    val quests by QuestDAO referrersOn Quests.mobId

    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        return if (other is MobDAO) other.id.value == id.value else super.equals(other)
    }

    override fun hashCode(): Int {
        return id.value
    }
}

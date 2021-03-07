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
import kotlinmud.mob.type.JobType
import kotlinmud.mob.type.MobCanonicalId
import kotlinmud.mob.type.Rarity
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.player.dao.PlayerDAO
import kotlinmud.room.dao.RoomDAO
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class MobDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MobDAO>(Mobs)

    var name by Mobs.name
    var brief by Mobs.brief
    var description by Mobs.description
    var hp by Mobs.hp
    var mana by Mobs.mana
    var mv by Mobs.mv
    var level by Mobs.level
    var race by Mobs.race.transform(
        { it.toString() },
        { RaceType.valueOf(it) }
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
    var wimpy by Mobs.wimpy
    var savingThrows by Mobs.savingThrows
    var isNpc by Mobs.isNpc
    var route by Mobs.route.transform(
        { it?.joinToString(", ") },
        { it?.split(", ")?.map { value -> value.toInt() } }
    )
    var lastRoute by Mobs.lastRoute
    var maxItems by Mobs.maxItems
    var maxWeight by Mobs.maxWeight
    var rarity by Mobs.rarity.transform(
        { it.toString() },
        { Rarity.valueOf(it) }
    )
    var canonicalId by Mobs.canonicalId.transform(
        { it.toString() },
        { it?.let { MobCanonicalId.valueOf(it) } }
    )
    var attributes by AttributesDAO referencedOn Mobs.attributesId
    var room by RoomDAO referencedOn Mobs.roomId
    val equipped by ItemDAO optionalReferrersOn Items.mobEquippedId
    val items by ItemDAO optionalReferrersOn Items.mobInventoryId
    val skills by SkillDAO referrersOn Skills.mobId
    val affects by AffectDAO optionalReferrersOn Affects.mobId
    var player by PlayerDAO optionalReferencedOn Mobs.playerId
    var mobCard by MobCardDAO optionalReferencedOn Mobs.mobCardId
    val currencies by CurrencyDAO referrersOn Currencies.mobId

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

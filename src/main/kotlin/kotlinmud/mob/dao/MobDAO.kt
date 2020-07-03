package kotlinmud.mob.dao

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.table.Items
import kotlinmud.mob.race.factory.createRaceFromString
import kotlinmud.mob.table.Mobs
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.Gender
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
        { createRaceFromString(it) }
    )
    var specialization by Mobs.specialization
    var disposition by Mobs.disposition.transform(
        { it.toString() },
        { Disposition.valueOf(it) }
    )
    var job by Mobs.job
    var gender by Mobs.gender.transform(
        { it.toString() },
        { Gender.valueOf(it) }
    )
    var gold by Mobs.gold
    var goldMin by Mobs.goldMin
    var goldMax by Mobs.goldMax
    var wimpy by Mobs.wimpy
    var savingThrows by Mobs.savingThrows
    var isNpc by Mobs.isNpc
    var route by Mobs.route
    var maxItems by Mobs.maxItems
    var maxWeight by Mobs.maxWeight
    var rarity by Mobs.rarity
    var attributes by AttributesDAO referencedOn Mobs.attributesId
    val equipped by ItemDAO referrersOn Items.mobEquippedId
    val items by ItemDAO referrersOn Items.mobInventoryId

    fun isIncapacitated(): Boolean {
        return disposition == Disposition.DEAD
    }
}

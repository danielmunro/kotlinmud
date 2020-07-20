package kotlinmud.player.dao

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.table.Attributes
import kotlinmud.attributes.type.Attribute
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.table.Mobs
import kotlinmud.player.table.MobCards
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class MobCardDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MobCardDAO>(MobCards)

    var experience by MobCards.experience
    var experiencePerLevel by MobCards.experiencePerLevel
    var trains by MobCards.trains
    var practices by MobCards.practices
    var bounty by MobCards.bounty
    var sacPoints by MobCards.sacPoints
    var hunger by MobCards.hunger
    var thirst by MobCards.thirst
    var skillPoints by MobCards.skillPoints
    val trainedAttributes by AttributesDAO referrersOn Attributes.mobCardId
    val mob by MobDAO optionalReferencedOn Mobs.mobCardId
    val player by PlayerDAO optionalReferencedOn Mobs.playerId

    fun calcTrained(attribute: Attribute): Int {
        return trainedAttributes.fold(0) { acc, it -> acc + it.getAttribute(attribute) }
    }
}

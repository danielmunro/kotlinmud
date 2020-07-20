package kotlinmud.player.dao

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.table.Attributes
import kotlinmud.attributes.type.Attribute
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.model.AddExperience
import kotlinmud.mob.model.Appetite
import kotlinmud.mob.table.Mobs
import kotlinmud.player.table.MobCards
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.sql.transactions.transaction

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
    var mob by MobDAO optionalReferencedOn Mobs.mobCardId
    var player by PlayerDAO optionalReferencedOn Mobs.playerId

    fun calcTrained(attribute: Attribute): Int {
        return trainedAttributes.fold(0) { acc, it -> acc + it.getAttribute(attribute) }
    }

    fun addExperience(level: Int, value: Int): AddExperience {
        val toLevelInitial = getExperienceToLevel(level)
        if (toLevelInitial < 0) {
            return AddExperience(0, true)
        }
        transaction {
            experience += value
        }
        var didLevel = false
        val toLevel = getExperienceToLevel(level)
        if (toLevel < 0) {
            didLevel = true
        }
        return AddExperience(experience, didLevel)
    }

    fun getAppetite(): Appetite {
        val race = transaction { mob!!.race }
        return Appetite(race.maxAppetite, race.maxThirst, hunger, thirst)
    }

    private fun getExperienceToLevel(level: Int): Int {
        return transaction { experiencePerLevel - experience + (experiencePerLevel * level) }
    }
}

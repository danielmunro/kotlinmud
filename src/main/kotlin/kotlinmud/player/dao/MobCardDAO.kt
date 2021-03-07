package kotlinmud.player.dao

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.table.Attributes
import kotlinmud.attributes.type.Attribute
import kotlinmud.mob.model.AddExperience
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType
import kotlinmud.player.table.FactionScores
import kotlinmud.player.table.MobCards
import kotlinmud.quest.dao.QuestDAO
import kotlinmud.quest.table.Quests
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.sql.transactions.transaction

class MobCardDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MobCardDAO>(MobCards)

    var mobName by MobCards.mobName
    var emailAddress by MobCards.emailAddress
    var level by MobCards.level
    var experience by MobCards.experience
    var experiencePerLevel by MobCards.experiencePerLevel
    var trains by MobCards.trains
    var practices by MobCards.practices
    var bounty by MobCards.bounty
    var sacPoints by MobCards.sacPoints
    var hunger by MobCards.hunger
    var thirst by MobCards.thirst
    var skillPoints by MobCards.skillPoints
    var loggedIn by MobCards.loggedIn
    var race by MobCards.race.transform(
        { it.toString() },
        { RaceType.valueOf(it) }
    )
    val trainedAttributes by AttributesDAO optionalReferrersOn Attributes.mobCardId
    val factionScores by FactionScoreDAO referrersOn FactionScores.mobCardId
    val quests by QuestDAO referrersOn Quests.mobCardId
    var player by PlayerDAO referencedOn MobCards.playerId

    fun calcTrained(attribute: Attribute): Int {
        return transaction { trainedAttributes.fold(0) { acc, it -> acc + it.getAttribute(attribute) } }
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

    fun isFull(race: Race): Boolean {
        return hunger == race.maxAppetite || thirst == race.maxThirst
    }

    private fun getExperienceToLevel(level: Int): Int {
        return transaction { experiencePerLevel - experience + (experiencePerLevel * level - 1) }
    }
}

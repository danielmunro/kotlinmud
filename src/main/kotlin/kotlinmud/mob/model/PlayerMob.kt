package kotlinmud.mob.model

import kotlinmud.faction.type.FactionType
import kotlinmud.quest.type.QuestStatus
import kotlinmud.quest.type.QuestType

class PlayerMob(
    mobArguments: MobArguments,
    var emailAddress: String,
    var experience: Int,
    var experienceToLevel: Int,
    var trains: Int,
    var practices: Int,
    var bounty: Int,
    var sacPoints: Int,
    var hunger: Int,
    var thirst: Int,
    var skillPoints: Int,
    var loggedIn: Boolean,
    var factionScores: MutableMap<FactionType, Int>,
    var quests: MutableMap<QuestType, QuestStatus>
) : Mob(mobArguments) {
    fun addExperience(level: Int, value: Int): AddExperience {
        val toLevelInitial = getExperienceToLevel(level)
        if (toLevelInitial < 0) {
            return AddExperience(0, true)
        }
        experience += value
        var didLevel = false
        val toLevel = getExperienceToLevel(level)
        if (toLevel < 0) {
            didLevel = true
        }
        return AddExperience(experience, didLevel)
    }

    private fun getExperienceToLevel(level: Int): Int {
        return experienceToLevel - experience + (experienceToLevel * level)
    }
}

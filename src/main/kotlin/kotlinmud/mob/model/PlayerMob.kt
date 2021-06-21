package kotlinmud.mob.model

import kotlinmud.faction.type.FactionType
import kotlinmud.mob.type.Role
import kotlinmud.mob.type.Standing
import kotlinmud.quest.model.QuestProgress
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
    var quests: MutableMap<QuestType, QuestProgress>,
    val role: Role,
    var standing: Standing,
) : Mob(mobArguments) {
    fun addExperience(value: Int): AddExperience {
        val toLevelInitial = getRemainingExperience()
        if (toLevelInitial < 0) {
            return AddExperience(0, true)
        }
        experience += value
        var didLevel = false
        val toLevel = getRemainingExperience()
        if (toLevel < 0) {
            didLevel = true
        }
        return AddExperience(value, didLevel)
    }

    fun isFull(): Boolean {
        return hunger == race.maxAppetite || thirst == race.maxThirst
    }

    fun getRemainingExperience(): Int {
        return (experienceToLevel * level + 1) - experience
    }
}

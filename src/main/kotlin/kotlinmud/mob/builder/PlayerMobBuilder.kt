package kotlinmud.mob.builder

import kotlinmud.faction.type.FactionType
import kotlinmud.mob.model.PlayerMob
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.Role
import kotlinmud.mob.type.Standing
import kotlinmud.quest.type.QuestStatus
import kotlinmud.quest.type.QuestType

class PlayerMobBuilder(private val mobService: MobService) : MobBuilder(mobService) {
    var emailAddress = ""
    var experience = 0
    var experienceToLevel = 0
    var trains = 0
    var practices = 0
    var bounty = 0
    var sacPoints = 0
    var hunger = 0
    var thirst = 0
    var skillPoints = 0
    var loggedIn = false
    var factionScores = mutableMapOf<FactionType, Int>()
    var quests = mutableMapOf<QuestType, QuestStatus>()
    var role = Role.Player
    var standing = Standing.Good

    override fun build(): PlayerMob {
        return PlayerMob(
            createMobArguments(),
            emailAddress,
            experience,
            experienceToLevel,
            trains,
            practices,
            bounty,
            sacPoints,
            hunger,
            thirst,
            skillPoints,
            loggedIn,
            factionScores,
            quests,
            role,
            standing,
        ).also {
            mobService.addMob(it)
        }
    }
}

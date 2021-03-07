package kotlinmud.mob.builder

import kotlinmud.faction.type.FactionType
import kotlinmud.mob.model.PlayerMob
import kotlinmud.mob.service.MobService
import kotlinmud.quest.type.QuestStatus
import kotlinmud.quest.type.QuestType

class PlayerMobBuilder(private val mobService: MobService) : MobBuilder(mobService) {
    private var emailAddress = ""
    private var experience = 0
    private var experienceToLevel = 0
    private var trains = 0
    private var practices = 0
    private var bounty = 0
    private var sacPoints = 0
    private var hunger = 0
    private var thirst = 0
    private var skillPoints = 0
    private var loggedIn = false
    private var factionScores = mutableMapOf<FactionType, Int>()
    private var quests = mutableMapOf<QuestType, QuestStatus>()

    fun emailAddress(value: String): PlayerMobBuilder {
        emailAddress = value
        return this
    }

    fun experience(value: Int): PlayerMobBuilder {
        experience = value
        return this
    }

    fun experienceToLevel(value: Int): PlayerMobBuilder {
        experienceToLevel = value
        return this
    }

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
            quests
        ).also {
            mobService.addMob(it)
        }
    }
}

package kotlinmud.mob.model

import kotlinmud.faction.type.FactionType
import kotlinmud.quest.type.QuestStatus
import kotlinmud.quest.type.QuestType

class PlayerMob(
    mobArguments: MobArguments,
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
) : Mob(mobArguments)

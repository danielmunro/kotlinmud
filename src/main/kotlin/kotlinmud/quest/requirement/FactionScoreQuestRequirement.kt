package kotlinmud.quest.requirement

import kotlinmud.faction.type.Faction
import kotlinmud.mob.model.PlayerMob
import kotlinmud.quest.type.QuestRequirement
import kotlinmud.quest.type.QuestRequirementType

class FactionScoreQuestRequirement(private val faction: Faction, val score: Int) : QuestRequirement {
    override val questRequirementType = QuestRequirementType.FACTION_SCORE

    override fun doesSatisfy(mob: PlayerMob): Boolean {
        return mob.factionScores[faction.type] ?: 0 > score
    }
}

package kotlinmud.mob.quest.requirement

import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.faction.type.Faction
import kotlinmud.mob.quest.type.QuestRequirement
import kotlinmud.mob.quest.type.QuestRequirementType

class FactionScoreQuestRequirement(private val faction: Faction, val score: Int) : QuestRequirement {
    override val questRequirementType = QuestRequirementType.FACTION_SCORE

    override fun doesSatisfy(mob: MobDAO): Boolean {
        return mob.mobCard!!.factionScores.find { it.faction.toString() == faction.name }?.score ?: 0 > score
    }
}
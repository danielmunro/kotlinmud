package kotlinmud.quest.impl.praetorians

import kotlinmud.faction.type.FactionType
import kotlinmud.mob.type.CurrencyType
import kotlinmud.quest.factory.createMobInRoomQuestRequirement
import kotlinmud.quest.factory.createRoomQuestRequirement
import kotlinmud.quest.type.Quest
import kotlinmud.quest.type.QuestType
import kotlinmud.quest.type.reward.CurrencyQuestReward
import kotlinmud.quest.type.reward.ExperienceQuestReward
import kotlinmud.quest.type.reward.FactionScoreQuestReward
import kotlinmud.type.CanonicalId

class FindPraetorianRecruiter : Quest {
    override val type = QuestType.FIND_CAPTAIN_BARTOK_PRAETORIANS
    override val name = "Find a recruiter for the Praetorian Guard"
    override val description = "yolo"
    override val acceptConditions = listOf(
        createRoomQuestRequirement(CanonicalId.FIND_RECRUITER_PRAETORIAN_GUARD),
    )
    override val submitConditions = listOf(
        createMobInRoomQuestRequirement(CanonicalId.PRAETORIAN_GUARD_RECRUITER_FOUND),
    )
    override val rewards = listOf(
        FactionScoreQuestReward(FactionType.PRAETORIAN_GUARD, 100),
        ExperienceQuestReward(1000),
        CurrencyQuestReward(CurrencyType.Gold, 1),
        CurrencyQuestReward(CurrencyType.Silver, 15),
    )
}

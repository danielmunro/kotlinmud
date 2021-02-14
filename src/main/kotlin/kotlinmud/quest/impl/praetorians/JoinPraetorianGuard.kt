package kotlinmud.quest.impl.praetorians

import kotlinmud.faction.type.FactionType
import kotlinmud.mob.type.CurrencyType
import kotlinmud.quest.factory.createMobInRoomQuestRequirement
import kotlinmud.quest.type.Quest
import kotlinmud.quest.type.QuestType
import kotlinmud.quest.type.reward.CurrencyQuestReward
import kotlinmud.quest.type.reward.ExperienceQuestReward
import kotlinmud.quest.type.reward.FactionScoreQuestReward
import kotlinmud.type.CanonicalId

class JoinPraetorianGuard : Quest {
    override val type = QuestType.JOIN_PRAETORIAN_GUARD
    override val name = "Talk to Captain Bartok of the Praetorian Guard"
    override val description = "yolo"
    override val acceptConditions = listOf(
        createMobInRoomQuestRequirement(CanonicalId.PRAETORIAN_GUARD_RECRUITER_FOUND),
    )
    override val submitConditions = listOf(
        createMobInRoomQuestRequirement(CanonicalId.PRAETORIAN_CAPTAIN_FOUND),
    )
    override val rewards = listOf(
        FactionScoreQuestReward(FactionType.PRAETORIAN_GUARD, 100),
        ExperienceQuestReward(1000),
        CurrencyQuestReward(CurrencyType.Gold, 1),
        CurrencyQuestReward(CurrencyType.Silver, 15),
        CurrencyQuestReward(CurrencyType.Copper, 50),
    )
}

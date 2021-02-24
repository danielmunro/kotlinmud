package kotlinmud.quest.impl.praetorians

import kotlinmud.faction.type.FactionType
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.CurrencyType
import kotlinmud.mob.type.MobCanonicalId
import kotlinmud.quest.factory.createMobInRoomQuestRequirement
import kotlinmud.quest.factory.createRoomQuestRequirement
import kotlinmud.quest.type.Quest
import kotlinmud.quest.type.QuestType
import kotlinmud.quest.type.reward.CurrencyQuestReward
import kotlinmud.quest.type.reward.ExperienceQuestReward
import kotlinmud.quest.type.reward.FactionScoreQuestReward
import kotlinmud.type.CanonicalId

class FindPraetorianRecruiter(private val mobService: MobService) : Quest {
    override val type = QuestType.FIND_PRAETORIAN_GUARD_RECRUITER
    override val level = 5
    override val name = "Find a recruiter for the Praetorian Guard"
    override val description = "yolo"
    override val acceptConditions = listOf(
        createRoomQuestRequirement(CanonicalId.FIND_RECRUITER_PRAETORIAN_GUARD),
    )
    override val submitConditions = listOf(
        createMobInRoomQuestRequirement(mobService, MobCanonicalId.PraetorianRecruiterEsmer),
    )
    override val rewards = listOf(
        FactionScoreQuestReward(FactionType.PRAETORIAN_GUARD, 100),
        ExperienceQuestReward(1000),
        CurrencyQuestReward(CurrencyType.Gold, 1),
        CurrencyQuestReward(CurrencyType.Silver, 15),
    )
}

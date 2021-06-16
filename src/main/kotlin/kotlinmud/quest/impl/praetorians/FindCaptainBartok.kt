package kotlinmud.quest.impl.praetorians

import kotlinmud.faction.type.FactionType
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.CurrencyType
import kotlinmud.mob.type.QuestGiver
import kotlinmud.quest.factory.createMobInRoomQuestRequirement
import kotlinmud.quest.factory.createPriorQuestRequirement
import kotlinmud.quest.type.Quest
import kotlinmud.quest.type.QuestType
import kotlinmud.quest.type.reward.CurrencyQuestReward
import kotlinmud.quest.type.reward.ExperienceQuestReward
import kotlinmud.quest.type.reward.FactionScoreQuestReward

class FindCaptainBartok(mobService: MobService) : Quest {
    override val type = QuestType.JOIN_PRAETORIAN_GUARD
    override val level = 3
    override val name = "Talk to Captain Bartok of the Praetorian Guard"
    override val brief = "tbd"
    override val description = "yolo"
    override val acceptConditions = listOf(
        createMobInRoomQuestRequirement(mobService, QuestGiver.PraetorianRecruiterEsmer),
        createPriorQuestRequirement(QuestType.FIND_PRAETORIAN_GUARD_RECRUITER),
    )
    override val submitConditions = listOf(
        createMobInRoomQuestRequirement(mobService, QuestGiver.PraetorianCaptainBartok),
    )
    override val rewards = listOf(
        FactionScoreQuestReward(FactionType.PRAETORIAN_GUARD, 100),
        ExperienceQuestReward(1000),
        CurrencyQuestReward(CurrencyType.Gold, 1),
        CurrencyQuestReward(CurrencyType.Silver, 15),
        CurrencyQuestReward(CurrencyType.Copper, 50),
    )
}

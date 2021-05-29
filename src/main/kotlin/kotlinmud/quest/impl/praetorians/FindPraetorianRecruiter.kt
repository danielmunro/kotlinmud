package kotlinmud.quest.impl.praetorians

import kotlinmud.faction.type.FactionType
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.CurrencyType
import kotlinmud.mob.type.QuestGiver
import kotlinmud.quest.factory.createMobInRoomQuestRequirement
import kotlinmud.quest.factory.createRoomQuestRequirement
import kotlinmud.quest.type.Quest
import kotlinmud.quest.type.QuestType
import kotlinmud.quest.type.reward.CurrencyQuestReward
import kotlinmud.quest.type.reward.ExperienceQuestReward
import kotlinmud.quest.type.reward.FactionScoreQuestReward
import kotlinmud.room.service.RoomService
import kotlinmud.type.RoomCanonicalId

class FindPraetorianRecruiter(private val mobService: MobService, private val roomService: RoomService) : Quest {
    override val type = QuestType.FIND_PRAETORIAN_GUARD_RECRUITER
    override val level = 5
    override val name = "Find a recruiter for the Praetorian Guard"
    override val description = "yolo"
    override val acceptConditions = listOf(
        createRoomQuestRequirement(
            roomService.findOne {
                it.canonicalId == RoomCanonicalId.FIND_RECRUITER_PRAETORIAN_GUARD
            }!!
        ),
    )
    override val submitConditions = listOf(
        createMobInRoomQuestRequirement(mobService, QuestGiver.PraetorianRecruiterEsmer),
    )
    override val rewards = listOf(
        FactionScoreQuestReward(FactionType.PRAETORIAN_GUARD, 100),
        ExperienceQuestReward(1000),
        CurrencyQuestReward(CurrencyType.Gold, 1),
        CurrencyQuestReward(CurrencyType.Silver, 15),
    )
}

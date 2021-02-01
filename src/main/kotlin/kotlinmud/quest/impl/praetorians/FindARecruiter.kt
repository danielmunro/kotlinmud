package kotlinmud.quest.impl.praetorians

import kotlinmud.faction.type.FactionType
import kotlinmud.io.service.RequestService
import kotlinmud.player.dao.FactionScoreDAO
import kotlinmud.quest.repository.findOrCreatePraetorianRecruiter1
import kotlinmud.quest.repository.findOrCreatePraetorianRecruiter2
import kotlinmud.quest.requirement.MobInRoomQuestRequirement
import kotlinmud.quest.type.Quest
import kotlinmud.quest.type.QuestType
import org.jetbrains.exposed.sql.transactions.transaction

class FindARecruiter : Quest {
    override val type = QuestType.JOIN_PRAETORIAN_GUARD
    override val name = "Find Recruiter Bartok for the Praetorian Guard"
    override val description = ""
    override val acceptConditions = listOf(
        MobInRoomQuestRequirement(findOrCreatePraetorianRecruiter1()),
    )
    override val submitConditions = listOf(
        MobInRoomQuestRequirement(findOrCreatePraetorianRecruiter2())
    )

    override fun reward(requestService: RequestService) {
        transaction {
            FactionScoreDAO.new {
                mobCard = requestService.getMobCard()
                score = 100
                faction = FactionType.PRAETORIAN_GUARD
            }
        }
    }
}

package kotlinmud.quest.impl.praetorians

import kotlinmud.faction.type.FactionType
import kotlinmud.io.service.RequestService
import kotlinmud.player.dao.FactionScoreDAO
import kotlinmud.quest.factory.createMobInRoomQuestRequirement
import kotlinmud.quest.type.Quest
import kotlinmud.quest.type.QuestType
import kotlinmud.type.CanonicalId
import org.jetbrains.exposed.sql.transactions.transaction

class FindARecruiter : Quest {
    override val type = QuestType.JOIN_PRAETORIAN_GUARD
    override val name = "Find Recruiter Bartok for the Praetorian Guard"
    override val description = "yolo"
    override val acceptConditions = listOf(
        createMobInRoomQuestRequirement(CanonicalId.PRAETORIAN_RECRUITER_1),
    )
    override val submitConditions = listOf(
        createMobInRoomQuestRequirement(CanonicalId.PRAETORIAN_RECRUITER_2),
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

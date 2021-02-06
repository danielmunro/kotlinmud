package kotlinmud.quest.impl.praetorians

import kotlinmud.faction.type.FactionType
import kotlinmud.io.service.RequestService
import kotlinmud.player.dao.FactionScoreDAO
import kotlinmud.quest.factory.createMobInRoomQuestRequirement
import kotlinmud.quest.factory.createRoomQuestRequirement
import kotlinmud.quest.type.Quest
import kotlinmud.quest.type.QuestType
import kotlinmud.type.CanonicalId
import org.jetbrains.exposed.sql.transactions.transaction

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

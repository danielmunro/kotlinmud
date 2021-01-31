package kotlinmud.mob.quest.impl.praetorians

import kotlinmud.io.service.RequestService
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.faction.type.Faction
import kotlinmud.mob.faction.type.FactionType
import kotlinmud.mob.quest.requirement.MobInRoomQuestRequirement
import kotlinmud.mob.quest.type.Quest
import kotlinmud.mob.quest.type.QuestType
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.JobType
import kotlinmud.player.dao.FactionScoreDAO
import kotlinmud.room.repository.findRoomById
import org.jetbrains.exposed.sql.transactions.transaction

class FindARecruiter : Quest {
    override val type = QuestType.JOIN_PRAETORIAN_GUARD
    override val name = "Find Recruiter Bartok for the Praetorian Guard"
    override val description = ""
    val mob = transaction {
        MobDAO.new {
            name = "Recruiter Bartok"
            room = findRoomById(1)
            job = JobType.QUEST_GIVER
        }
    }
    override val requirements = listOf(
        MobInRoomQuestRequirement(mob),
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
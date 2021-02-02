package kotlinmud.quest.service

import kotlinmud.mob.dao.MobDAO
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.quest.dao.QuestDAO
import kotlinmud.quest.helper.createQuestList
import kotlinmud.quest.repository.findQuestByMobCardAndType
import kotlinmud.quest.requirement.MobInRoomQuestRequirement
import kotlinmud.quest.type.Quest
import kotlinmud.quest.type.QuestStatus
import org.jetbrains.exposed.sql.transactions.transaction

class QuestService {
    private val quests = createQuestList()

    fun getAcceptableQuestsForMob(mob: MobDAO): List<Quest> {
        return quests.filter {
            it.acceptConditions.find { req ->
                req is MobInRoomQuestRequirement && req.doesSatisfy(mob)
            } != null
        }
    }

    fun accept(mobCard: MobCardDAO, quest: Quest) {
        findQuestByMobCardAndType(mobCard, quest.type)?.let {
            return
        }

        transaction {
            QuestDAO.new {
                this.mobCard = mobCard
                this.quest = quest.type
                status = QuestStatus.INITIALIZED
            }
        }
    }
}

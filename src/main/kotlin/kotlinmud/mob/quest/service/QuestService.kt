package kotlinmud.mob.quest.service

import kotlinmud.mob.quest.dao.QuestDAO
import kotlinmud.mob.quest.helper.createQuestList
import kotlinmud.mob.quest.repository.findQuestByMobCardAndType
import kotlinmud.mob.quest.type.Quest
import kotlinmud.mob.quest.type.QuestStatus
import kotlinmud.player.dao.MobCardDAO
import org.jetbrains.exposed.sql.transactions.transaction

class QuestService {
    private val quests = createQuestList()

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
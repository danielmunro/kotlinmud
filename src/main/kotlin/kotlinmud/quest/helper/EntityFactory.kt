package kotlinmud.quest.helper

import kotlinmud.player.dao.MobCardDAO
import kotlinmud.quest.dao.QuestDAO
import kotlinmud.quest.type.QuestStatus
import kotlinmud.quest.type.QuestType
import org.jetbrains.exposed.sql.transactions.transaction

fun createQuestEntity(mobCard: MobCardDAO, questType: QuestType) {
    transaction {
        QuestDAO.new {
            this.mobCard = mobCard
            quest = questType
            status = QuestStatus.INITIALIZED
        }
    }
}

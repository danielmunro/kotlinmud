package kotlinmud.quest.service

import kotlinmud.mob.dao.MobDAO
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.quest.dao.QuestDAO
import kotlinmud.quest.helper.createQuestList
import kotlinmud.quest.requirement.MobInRoomQuestRequirement
import kotlinmud.quest.type.Quest
import kotlinmud.quest.type.QuestStatus
import kotlinmud.quest.type.QuestType
import org.jetbrains.exposed.sql.transactions.transaction

class QuestService {
    private val quests = createQuestList()

    fun getAcceptableQuestsForMob(mob: MobDAO): List<Quest> {
        val mobQuests = transaction { mob.mobCard!!.quests }
        val questMap = mutableMapOf<QuestType, Int>()
        transaction {
            mobQuests.forEach {
                questMap[it.quest] = 1
            }
        }
        return quests.filter {
            !questMap.containsKey(it.type) && it.acceptConditions.find { req ->
                req is MobInRoomQuestRequirement && req.doesSatisfy(mob)
            } != null
        }
    }

    fun accept(mobCard: MobCardDAO, quest: Quest) {
        transaction {
            QuestDAO.new {
                this.mobCard = mobCard
                this.quest = quest.type
                status = QuestStatus.INITIALIZED
            }
        }
    }
}

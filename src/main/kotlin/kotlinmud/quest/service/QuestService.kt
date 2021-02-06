package kotlinmud.quest.service

import kotlinmud.mob.dao.MobDAO
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.quest.dao.QuestDAO
import kotlinmud.quest.helper.createQuestList
import kotlinmud.quest.requirement.MobInRoomQuestRequirement
import kotlinmud.quest.table.Quests
import kotlinmud.quest.type.Quest
import kotlinmud.quest.type.QuestStatus
import kotlinmud.quest.type.QuestType
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class QuestService {
    private val quests = createQuestList()

    fun findByType(type: QuestType): Quest? {
        return quests.find { it.type == type }
    }

    fun getAcceptableQuestsForMob(mob: MobDAO): List<Quest> {
        val questMap = createQuestMap(transaction { mob.mobCard!!.quests.toList() })
        return quests.filter {
            !questMap.containsKey(it.type) && it.acceptConditions.find { req ->
                req is MobInRoomQuestRequirement && req.doesSatisfy(mob)
            } != null
        }
    }

    fun getAcceptedQuestsForMob(mob: MobDAO): List<Quest> {
        val questMap = createQuestMap(
            transaction {
                mob.mobCard!!.quests.toList().filter { it.status != QuestStatus.SUBMITTED }
            }
        )
        return quests.filter { questMap.containsKey(it.type) }
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

    fun abandon(mobCard: MobCardDAO, quest: Quest) {
        transaction {
            QuestDAO.wrapRow(
                Quests.select {
                    Quests.mobCardId eq mobCard.id and (Quests.quest eq quest.type.toString())
                }.first()
            ).delete()
        }
    }

    fun getLog(mobCard: MobCardDAO): List<Quest> {
        val questMap = createQuestMap(transaction { mobCard.quests.toList() })
        return quests.filter { questMap[it.type] !== null }
    }

    private fun createQuestMap(mobQuests: List<QuestDAO>): MutableMap<QuestType, Int> {
        val questMap = mutableMapOf<QuestType, Int>()
        transaction {
            mobQuests.forEach {
                questMap[it.quest] = 1
            }
        }
        return questMap
    }
}

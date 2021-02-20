package kotlinmud.quest.service

import kotlinmud.mob.dao.MobDAO
import kotlinmud.player.dao.FactionScoreDAO
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.player.repository.findFactionScoreByType
import kotlinmud.quest.dao.QuestDAO
import kotlinmud.quest.helper.createQuestList
import kotlinmud.quest.repository.findQuestByMobCardAndType
import kotlinmud.quest.table.Quests
import kotlinmud.quest.type.Quest
import kotlinmud.quest.type.QuestStatus
import kotlinmud.quest.type.QuestType
import kotlinmud.quest.type.reward.CurrencyQuestReward
import kotlinmud.quest.type.reward.ExperienceQuestReward
import kotlinmud.quest.type.reward.FactionScoreQuestReward
import kotlinmud.quest.type.reward.ItemQuestReward
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
            val notSatisfied = it.acceptConditions.find { req -> !req.doesSatisfy(mob) }
            !questMap.containsKey(it.type) && notSatisfied == null
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

    fun getSubmittableQuestsForMob(mob: MobDAO): List<Quest> {
        val questMap = createQuestMap(transaction { mob.mobCard!!.quests.toList() })
        return quests.filter {
            val notSatisfied = it.submitConditions.find { req -> !req.doesSatisfy(mob) }
            questMap.containsKey(it.type) && notSatisfied == null
        }
    }

    fun submit(mobCard: MobCardDAO, quest: Quest) {
        findQuestByMobCardAndType(mobCard, quest.type)?.let {
            transaction { it.status = QuestStatus.SUBMITTED }
        }
        reward(mobCard, quest)
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

    fun reward(mobCard: MobCardDAO, quest: Quest) {
        val mob = transaction { mobCard.mob }
        quest.rewards.forEach {
            if (it is ExperienceQuestReward) {
                mobCard.addExperience(mob.level, it.amount)
            } else if (it is CurrencyQuestReward) {
                mob.addCurrency(it.currencyType, it.amount)
            } else if (it is FactionScoreQuestReward) {
                transaction {
                    findFactionScoreByType(mobCard, it.factionType)?.let { entity ->
                        entity.score = it.score
                    } ?: run {
                        FactionScoreDAO.new {
                            this.mobCard = mobCard
                            faction = it.factionType
                            score = it.score
                        }
                    }
                }
            } else if (it is ItemQuestReward) {
                val item = it.createItem()
                transaction { item.mobInventory = mob }
            }
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

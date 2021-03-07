package kotlinmud.quest.service

import kotlinmud.mob.model.Mob
import kotlinmud.mob.model.PlayerMob
import kotlinmud.mob.service.MobService
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.quest.dao.QuestDAO
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

class QuestService(private val mobService: MobService, private val quests: List<Quest>) {
    fun findByType(type: QuestType): Quest? {
        return quests.find { it.type == type }
    }

    fun getAcceptableQuestsForMob(mob: Mob): List<Quest> {
        val questMap = createQuestMap(transaction { mob.mobCard!!.quests.toList() })
        return quests.filter {
            val notSatisfied = it.acceptConditions.find { req -> !req.doesSatisfy(mob) }
            !questMap.containsKey(it.type) && notSatisfied == null
        }
    }

    fun getAcceptedQuestsForMob(mob: Mob): List<Quest> {
        val questMap = createQuestMap(
            transaction {
                mob.mobCard!!.quests.toList().filter { it.status != QuestStatus.SUBMITTED }
            }
        )
        return quests.filter { questMap.containsKey(it.type) }
    }

    fun getSubmittableQuestsForMob(mob: Mob): List<Quest> {
        val questMap = createQuestMap(transaction { mob.mobCard!!.quests.toList() })
        return quests.filter {
            val notSatisfied = it.submitConditions.find { req -> !req.doesSatisfy(mob) }
            questMap.containsKey(it.type) && notSatisfied == null
        }
    }

    fun submit(playerMob: PlayerMob, quest: Quest) {
        playerMob.quests[quest.type]?.let {
            playerMob.quests[quest.type] = QuestStatus.SUBMITTED
        }
        reward(playerMob, quest)
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

    fun reward(mob: PlayerMob, quest: Quest) {
        quest.rewards.forEach {
            if (it is ExperienceQuestReward) {
                mob.addExperience(mob.level, it.amount)
            } else if (it is CurrencyQuestReward) {
                mob.addCurrency(it.currencyType, it.amount)
            } else if (it is FactionScoreQuestReward) {
                mob.factionScores[it.factionType]?.let { num ->
                    mob.factionScores[it.factionType] = num + it.score
                } ?: run {
                    mob.factionScores[it.factionType] = it.score
                }
            } else if (it is ItemQuestReward) {
                mob.items.add(it.createItem())
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

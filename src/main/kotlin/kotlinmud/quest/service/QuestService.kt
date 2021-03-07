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

class QuestService(private val quests: List<Quest>) {
    fun findByType(type: QuestType): Quest? {
        return quests.find { it.type == type }
    }

    fun getAcceptableQuestsForMob(mob: PlayerMob): List<Quest> {
        return quests.filter {
            val notSatisfied = it.acceptConditions.find { req -> !req.doesSatisfy(mob) }
            !mob.quests.containsKey(it.type) && notSatisfied == null
        }
    }

    fun getAcceptedQuestsForMob(mob: PlayerMob): List<Quest> {
        return quests.filter { mob.quests.containsKey(it.type) && mob.quests[it.type] != QuestStatus.SUBMITTED }
    }

    fun getSubmittableQuestsForMob(mob: PlayerMob): List<Quest> {
        return quests.filter {
            val notSatisfied = it.submitConditions.find { req -> !req.doesSatisfy(mob) }
            mob.quests.containsKey(it.type) && notSatisfied == null
        }
    }

    fun submit(mob: PlayerMob, quest: Quest) {
        mob.quests[quest.type]?.let {
            mob.quests[quest.type] = QuestStatus.SUBMITTED
        }
        reward(mob, quest)
    }

    fun accept(mob: PlayerMob, quest: Quest) {
        mob.quests[quest.type] = QuestStatus.INITIALIZED
    }

    fun abandon(mob: PlayerMob, quest: Quest) {
        mob.quests.remove(quest.type)
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

    fun getLog(mob: PlayerMob): List<Quest> {
        return quests.filter { mob.quests[it.type] !== null }
    }
}

package kotlinmud.action.impl.quest

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.submittableQuest
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.item.model.Item
import kotlinmud.quest.model.Quest
import kotlinmud.quest.type.reward.CurrencyQuestReward
import kotlinmud.quest.type.reward.ExperienceQuestReward
import kotlinmud.quest.type.reward.FactionScoreQuestReward
import kotlinmud.quest.type.reward.ItemQuestReward
import kotlinmud.quest.type.reward.QuestReward

fun createQuestSubmitAction(): Action {
    return ActionBuilder(Command.QUEST_SUBMIT).also {
        it.syntax = submittableQuest()
    } build {
        val quest = it.get<Quest>(Syntax.SUBMITTABLE_QUEST)
        it.submitQuest(quest)
        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("you submit the quest: `${quest.name}`\nYour reward:\n${quest.rewards.fold("") { acc: String, reward: QuestReward -> acc + getRewardString(reward) + "\n" }.trim()} ")
                .toObservers("${it.getMob()} submits the quest: `${quest.name}`")
                .build()
        )
    }
}

fun getRewardString(reward: QuestReward): String {
    return when (reward) {
        is CurrencyQuestReward ->
            "${reward.amount} ${reward.currencyType.toString().toLowerCase()}"
        is ExperienceQuestReward -> "${reward.amount} experience"
        is ItemQuestReward ->
            reward.createItems().fold("") { acc: String, item: Item -> acc + item.name + "\n" }

        is FactionScoreQuestReward ->
            "${reward.amount} points with the ${reward.factionType.value}"
        else -> ""
    }
}

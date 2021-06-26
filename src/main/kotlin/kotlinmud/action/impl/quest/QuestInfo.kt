package kotlinmud.action.impl.quest

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.acceptedQuest
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.type.Syntax
import kotlinmud.quest.model.Quest
import kotlinmud.quest.requirement.FactionScoreQuestRequirement
import kotlinmud.quest.requirement.ItemQuestRequirement
import kotlinmud.quest.requirement.LevelQuestRequirement
import kotlinmud.quest.requirement.MobInRoomQuestRequirement
import kotlinmud.quest.requirement.MobKillQuestRequirement
import kotlinmud.quest.requirement.PriorQuestRequirement
import kotlinmud.quest.requirement.RoomQuestRequirement

fun createQuestInfoAction(): Action {
    return ActionBuilder(Command.QUEST_INFO).also {
        it.syntax = acceptedQuest()
    } build {
        val quest = it.get<Quest>(Syntax.ACCEPTED_QUEST)
        it.createOkResponse(
            messageToActionCreator(
"""Information regarding the quest: ${quest.name}
    
${quest.description}

Submit conditions include:
${quest.submitConditions.joinToString("\n") { req ->
                    when (req) {
                        is MobKillQuestRequirement -> "kill ${req.amount} ${req.mobName}"
                        is FactionScoreQuestRequirement -> "gain ${req.amount} points with the ${req.faction.name}"
                        is ItemQuestRequirement -> "obtain ${req.amount} ${req.itemName}"
                        is LevelQuestRequirement -> "get to ${req.amount} level"
                        is MobInRoomQuestRequirement -> "find ${req.mobName}"
                        is PriorQuestRequirement -> "complete a previous quest"
                        is RoomQuestRequirement -> "return to ${req.room.name}"
                        else -> ""
                    }
                }}
"""
            )
        )
    }
}

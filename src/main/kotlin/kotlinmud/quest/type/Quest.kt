package kotlinmud.quest.type

import kotlinmud.io.service.RequestService

interface Quest {
    val type: QuestType
    val name: String
    val description: String
    val requirements: List<QuestRequirement>
    fun reward(requestService: RequestService)
}
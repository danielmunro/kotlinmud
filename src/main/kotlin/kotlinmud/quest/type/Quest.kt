package kotlinmud.quest.type

import kotlinmud.helper.Identifiable
import kotlinmud.io.service.RequestService

interface Quest : Identifiable {
    val type: QuestType
    override val name: String
    val description: String
    val acceptConditions: List<QuestRequirement>
    val submitConditions: List<QuestRequirement>
    fun reward(requestService: RequestService)
}

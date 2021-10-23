package kotlinmud.event.observer.impl.kill

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.KillEvent
import kotlinmud.event.observer.type.Observer
import kotlinmud.mob.model.PlayerMob
import kotlinmud.quest.requirement.MobKillQuestRequirement
import kotlinmud.quest.service.QuestService

class AddQuestKillObserver(private val questService: QuestService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        val killEvent = event.subject as KillEvent
        if (killEvent.victor is PlayerMob) {
            val quests = questService.getQuestMap()
            killEvent.victor.quests.forEach {
                val quest = quests[it.key]!!
                quest.submitConditions.find { condition -> condition is MobKillQuestRequirement && condition.mobId == killEvent.vanquished.id }?.let { _ ->
                    it.value.counter += 1
                }
            }
        }
    }
}

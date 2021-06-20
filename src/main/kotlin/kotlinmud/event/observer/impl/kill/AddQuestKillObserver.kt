package kotlinmud.event.observer.impl.kill

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.KillEvent
import kotlinmud.event.observer.type.Observer
import kotlinmud.helper.logger
import kotlinmud.io.service.ClientService
import kotlinmud.mob.model.PlayerMob

class AddQuestKillObserver(private val clientService: ClientService) : Observer {
    private val logger = logger(this)

    override suspend fun <T> invokeAsync(event: Event<T>) {
        val killEvent = event.subject as KillEvent

        killEvent.vanquished.partOfQuest?.let {
            if (killEvent.victor is PlayerMob) {
                killEvent.victor.quests[it]?.let { quest ->
                    quest.counter += 1
                    clientService.getClientForMob(killEvent.victor)?.writePrompt("quest kill: ${quest.counter}")
                    logger.info("increment quest kill counter :: quest {} :: kill counter {}", it.toString(), quest.counter)
                }
            }
        }
    }
}

package kotlinmud.event.observer.impl.kill

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.KillEvent
import kotlinmud.event.observer.type.Observer
import kotlinmud.mob.model.PlayerMob

class AddQuestKillObserver : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        val killEvent = event.subject as KillEvent

        killEvent.vanquished.partOfQuest?.let {
            if (killEvent.victor is PlayerMob) {
                killEvent.victor.quests[it]?.let { quest -> quest.counter += 1 }
            }
        }
    }
}

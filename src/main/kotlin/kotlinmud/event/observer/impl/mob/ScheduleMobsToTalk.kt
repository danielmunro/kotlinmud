package kotlinmud.event.observer.impl.mob

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.helper.time.eventually
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.service.MobService
import kotlinx.coroutines.runBlocking

class ScheduleMobsToTalk(private val mobService: MobService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        mobService.findMobs {
            it.messages.isNotEmpty()
        }.forEach {
            eventually {
                runBlocking {
                    mobService.sendMessageToRoom(
                        MessageBuilder()
                            .toObservers("$it says, \"${it.messages.random()}\"")
                            .build(),
                        it.room,
                        it,
                    )
                }
            }
        }
    }
}

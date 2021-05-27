package kotlinmud.event.observer.impl

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.SendMessageToRoomEvent
import kotlinmud.event.observer.type.Observer
import kotlinmud.io.model.Client
import kotlinmud.io.service.ServerService
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.Disposition

class SendMessageToRoomObserver(private val mobService: MobService, private val serverService: ServerService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        val messageEvent = event.subject as SendMessageToRoomEvent
        val message = messageEvent.message
        serverService.getClientsFromMobs(mobService.findPlayerMobs())
            .filter {
                it.mob!!.room == messageEvent.room
                        && it.mob!!.disposition != Disposition.SLEEPING
                        && it.mob!!.disposition != Disposition.DEAD
            }
            .forEach {
                when (it.mob) {
                    messageEvent.actionCreator -> sendIfSet(it, message.toActionCreator, message.sendPrompt)
                    messageEvent.target -> sendIfSet(it, message.toTarget)
                    else -> sendIfSet(it, message.toObservers)
                }
            }
    }

    private fun sendIfSet(client: Client, message: String?, writePrompt: Boolean = false) {
        message?.let {
            if (writePrompt) {
                client.writePrompt(it)
            } else {
                client.write(it + "\n")
            }
        }
    }
}

package kotlinmud.event.observer.impl

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.SendMessageToRoomEvent
import kotlinmud.event.observer.type.Observer
import kotlinmud.io.model.Client
import kotlinmud.io.service.ServerService
import kotlinmud.mob.repository.findMobsForRoom

class SendMessageToRoomObserver(private val serverService: ServerService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        val messageEvent = event.subject as SendMessageToRoomEvent
        val mobs = findMobsForRoom(messageEvent.room)
        val message = messageEvent.message
        serverService.getClientsFromMobs(mobs)
            .filter { !it.mob!!.isSleeping() && !it.mob!!.isIncapacitated() }
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

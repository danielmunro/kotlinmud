package kotlinmud.event.observer.impl

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.SendMessageToRoomEvent
import kotlinmud.event.observer.type.Observer
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
                    messageEvent.actionCreator -> if (message.sendPrompt) it.writePrompt(message.toActionCreator) else it.write(message.toActionCreator + "\n")
                    messageEvent.target -> it.write(message.toTarget + "\n")
                    else -> it.write(message.toObservers + "\n")
                }
            }
    }
}

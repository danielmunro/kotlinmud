package kotlinmud.event.observer.impl

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.SendMessageToRoomEvent
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType
import kotlinmud.io.service.ServerService
import kotlinmud.mob.service.MobService

class SendMessageToRoomObserver(private val serverService: ServerService, private val mobService: MobService) :
    Observer {
    override val eventType: EventType = EventType.SEND_MESSAGE_TO_ROOM

    override fun <T> processEvent(event: Event<T>) {
        val messageEvent = event.subject as SendMessageToRoomEvent
        val mobs = mobService.getMobsForRoom(messageEvent.room)
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

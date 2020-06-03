package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.event.SendMessageToRoomEvent
import kotlinmud.event.observer.Observer
import kotlinmud.io.NIOServer
import kotlinmud.mob.service.MobService

class SendMessageToRoomObserver(private val server: NIOServer, private val mobService: MobService) :
    Observer {
    override val eventType: EventType = EventType.SEND_MESSAGE_TO_ROOM

    override fun <T> processEvent(event: Event<T>) {
        val messageEvent = event.subject as SendMessageToRoomEvent
        val mobs = mobService.getMobsForRoom(messageEvent.room)
        val message = messageEvent.message
        server.getClientsFromMobs(mobs)
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

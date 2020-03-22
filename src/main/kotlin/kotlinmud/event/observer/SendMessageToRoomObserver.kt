package kotlinmud.event.observer

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.event.event.SendMessageToRoomEvent
import kotlinmud.io.Server
import kotlinmud.service.MobService

class SendMessageToRoomObserver(private val server: Server, private val mobService: MobService) : Observer {
    override val eventTypes: List<EventType> = listOf(EventType.SEND_MESSAGE_TO_ROOM)

    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        val messageEvent = event.subject as SendMessageToRoomEvent
        val mobs = mobService.getMobsForRoom(messageEvent.room)
        val message = messageEvent.message
        server.getClientsFromMobs(mobs)
            .filter { !it.mob.isSleeping() && !it.mob.isIncapacitated() }
            .forEach {
                when (it.mob) {
                    messageEvent.actionCreator -> it.write(message.toActionCreator)
                    messageEvent.target -> it.write(message.toTarget)
                    else -> it.write(message.toObservers)
                }
            }
        @Suppress("UNCHECKED_CAST")
        return EventResponse(event as A)
    }
}

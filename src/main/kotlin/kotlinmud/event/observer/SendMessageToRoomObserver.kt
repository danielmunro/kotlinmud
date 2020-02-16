package kotlinmud.event.observer

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.event.event.SendMessageToRoomEvent
import kotlinmud.event.response.SendMessageToRoomResponse
import kotlinmud.io.Server
import kotlinmud.service.MobService

class SendMessageToRoomObserver(private val server: Server, private val mobService: MobService) : Observer {
    override val eventTypes: List<EventType> = listOf(EventType.SEND_MESSAGE_TO_ROOM)

    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        if (event.subject is SendMessageToRoomEvent) {
            val mobs = mobService.getMobsForRoom(event.subject.room)
            val message = event.subject.message
            server.getClientsFromMobs(mobs).forEach {
                when (it.mob) {
                    event.subject.actionCreator -> it.write(message.toActionCreator)
                    event.subject.target -> it.write(message.toTarget)
                    else -> it.write(message.toObservers)
                }
            }
        }
        return SendMessageToRoomResponse(event as A)
    }
}

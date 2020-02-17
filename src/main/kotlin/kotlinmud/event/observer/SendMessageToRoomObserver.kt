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
            val clients = server.getClientsFromMobs(mobs)
//            println("=== MESSAGE RECEIVED ===")
//            println("-- message: $message")
//            println("-- room: ${event.subject.room.name}")
//            println("-- actionCreator: ${event.subject.actionCreator.name}")
//            println("-- mob count: ${mobs.size}")
//            println("-- client count: ${clients.size}")
//            println("=======================")
            clients.forEach {
//                println("${it.mob.name}: matches: AC: ${if (it.mob == event.subject.actionCreator) "YES" else "NO"}, target: ${if (it.mob == event.subject.target) "YES" else "NO"}")
                when (it.mob) {
                    event.subject.actionCreator -> it.write(message.toActionCreator)
                    event.subject.target -> it.write(message.toTarget)
                    else -> it.write(message.toObservers)
                }
            }
        }
        @Suppress("UNCHECKED_CAST")
        return SendMessageToRoomResponse(event as A)
    }
}

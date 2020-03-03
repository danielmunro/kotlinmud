package kotlinmud.event.observer

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.event.WrongEventTypeException
import kotlinmud.event.event.SocialEvent
import kotlinmud.io.Message
import kotlinmud.io.Server
import kotlinmud.io.SocialChannel
import kotlinmud.mob.Mob
import kotlinmud.room.Room
import kotlinmud.service.MobService

class SocialDistributorObserver(private val server: Server, private val mobService: MobService) : Observer {
    override val eventTypes: List<EventType> = listOf(EventType.SOCIAL)

    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        if (event.subject !is SocialEvent) {
            throw WrongEventTypeException()
        }

        val soc = event.subject.social
        when (soc.channel) {
            SocialChannel.SAY -> sayToRoom(soc.mob, soc.room, soc.message)
            SocialChannel.TELL -> tellMob(soc.target!!, soc.message)
            SocialChannel.YELL -> yellToArea(soc.mob, soc.room, soc.message)
            SocialChannel.GOSSIP -> gossipToClients(soc.mob, soc.message)
        }
        @Suppress("UNCHECKED_CAST")
        return EventResponse(event as A)
    }

    private fun yellToArea(mob: Mob, room: Room, message: Message) {
        server.getClients().forEach {
            val clientRoom = mobService.getRoomForMob(it.mob)
            if (it.mob != mob && clientRoom.area == room.area) {
                it.write(message.toObservers)
            }
        }
    }

    private fun gossipToClients(mob: Mob, message: Message) {
        server.getClients().forEach {
            if (it.mob != mob) {
                it.write(message.toObservers)
            }
        }
    }

    private fun tellMob(target: Mob, message: Message) {
        val clients = server.getClientsFromMobs(listOf(target))
        clients[0].write(message.toTarget)
    }

    private fun sayToRoom(mob: Mob, room: Room, message: Message) {
        val mobs = mobService.getMobsForRoom(room)
        server.getClientsFromMobs(mobs).forEach {
            if (it.mob != mob) {
                it.write(message.toObservers)
            }
        }
    }
}

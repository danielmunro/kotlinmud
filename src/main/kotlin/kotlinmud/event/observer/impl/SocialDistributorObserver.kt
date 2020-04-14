package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.event.SocialEvent
import kotlinmud.event.observer.Observer
import kotlinmud.io.Message
import kotlinmud.io.NIOServer
import kotlinmud.mob.Mob
import kotlinmud.service.MobService
import kotlinmud.social.SocialChannel
import kotlinmud.world.room.Room

class SocialDistributorObserver(private val server: NIOServer, private val mobService: MobService) :
    Observer {
    override val eventType: EventType = EventType.SOCIAL

    override fun <T> processEvent(event: Event<T>) {
        val socialEvent = event.subject as SocialEvent
        val soc = socialEvent.social
        when (soc.channel) {
            SocialChannel.SAY -> sayToRoom(soc.mob, soc.room, soc.message)
            SocialChannel.TELL -> tellMob(soc.target!!, soc.message)
            SocialChannel.YELL -> yellToArea(soc.mob, soc.room, soc.message)
            SocialChannel.GOSSIP -> gossipToClients(soc.mob, soc.message)
        }
    }

    private fun yellToArea(mob: Mob, room: Room, message: Message) {
        server.getClients().forEach {
            val clientRoom = mobService.getRoomForMob(it.mob!!)
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
        if (clients.isNotEmpty()) {
            clients[0].write(message.toTarget)
        }
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

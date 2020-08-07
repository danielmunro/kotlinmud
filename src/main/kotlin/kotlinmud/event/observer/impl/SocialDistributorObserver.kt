package kotlinmud.event.observer.impl

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.SocialEvent
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType
import kotlinmud.io.model.Message
import kotlinmud.io.service.ServerService
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.repository.findMobsForRoom
import kotlinmud.player.social.SocialChannel
import kotlinmud.room.dao.RoomDAO
import org.jetbrains.exposed.sql.transactions.transaction

class SocialDistributorObserver(private val serverService: ServerService) : Observer {
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

    private fun yellToArea(mob: MobDAO, room: RoomDAO, message: Message) {
        serverService.getClients().forEach {
            val clientRoom = transaction { it.mob!!.room }
            if (it.mob != mob && clientRoom.area == room.area) {
                it.write(message.toObservers)
            }
        }
    }

    private fun gossipToClients(mob: MobDAO, message: Message) {
        serverService.getClients().forEach {
            if (it.mob != mob) {
                it.write(message.toObservers)
            }
        }
    }

    private fun tellMob(target: MobDAO, message: Message) {
        val clients = serverService.getClientsFromMobs(listOf(target))
        if (clients.isNotEmpty()) {
            clients[0].write(message.toTarget)
        }
    }

    private fun sayToRoom(mob: MobDAO, room: RoomDAO, message: Message) {
        val mobs = findMobsForRoom(room)
        serverService.getClientsFromMobs(mobs).forEach {
            if (it.mob != mob) {
                it.write(message.toObservers)
            }
        }
    }
}

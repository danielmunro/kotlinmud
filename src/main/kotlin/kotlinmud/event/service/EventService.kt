package kotlinmud.event.service

import kotlinmud.event.factory.createSendMessageToRoomEvent
import kotlinmud.event.impl.Event
import kotlinmud.event.impl.SendMessageToRoomEvent
import kotlinmud.event.observer.type.Observer
import kotlinmud.io.factory.createDeathMessage
import kotlinmud.mob.dao.MobDAO
import org.jetbrains.exposed.sql.transactions.transaction

class EventService {
    var observers: List<Observer> = listOf()

    fun <T> publish(event: Event<T>) {
        observers.filter { it.eventType == event.eventType }
            .forEach { transaction { it.processEvent(event) } }
    }

    fun publishRoomMessage(event: Event<SendMessageToRoomEvent>) {
        publish(event)
    }

    fun publishDeath(mob: MobDAO) {
        publish(createSendMessageToRoomEvent(createDeathMessage(mob), mob.room, mob))
    }
}

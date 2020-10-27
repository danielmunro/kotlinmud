package kotlinmud.event.service

import kotlinmud.event.factory.createSendMessageToRoomEvent
import kotlinmud.event.impl.Event
import kotlinmud.event.impl.FightRoundEvent
import kotlinmud.event.impl.SendMessageToRoomEvent
import kotlinmud.event.observer.type.ObserverList
import kotlinmud.io.factory.createDeathMessage
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.fight.Round
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import org.jetbrains.exposed.sql.transactions.transaction

class EventService {
    var observers: ObserverList = mapOf()

    suspend fun <T> publish(event: Event<T>) {
        return (observers[event.eventType] ?: return).map {
            GlobalScope.async { it.invokeAsync(event) }
        }.asFlow().collect { it.await() }
    }

    suspend fun publishRoomMessage(event: Event<SendMessageToRoomEvent>) {
        publish(event)
    }

    suspend fun publishDeath(mob: MobDAO) {
        publish(transaction { createSendMessageToRoomEvent(createDeathMessage(mob), mob.room, mob) })
    }

    suspend fun publishFightRound(round: Round) {
        publish(FightRoundEvent(round))
    }
}

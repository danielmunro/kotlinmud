package kotlinmud.event.service

import kotlinmud.event.factory.createSendMessageToRoomEvent
import kotlinmud.event.impl.Event
import kotlinmud.event.impl.FightRoundEvent
import kotlinmud.event.impl.SendMessageToRoomEvent
import kotlinmud.event.observer.type.Observer
import kotlinmud.io.factory.createDeathMessage
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.fight.Round
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.transactions.transaction

class EventService {
    var observers: Observer = mapOf()

    suspend fun <T> publish(event: Event<T>) {
        (observers[event.eventType] ?: return).map {
            GlobalScope.launch { transaction { it(event) } }
        }.joinAll()
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

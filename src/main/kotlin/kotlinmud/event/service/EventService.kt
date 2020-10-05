package kotlinmud.event.service

import kotlinmud.event.factory.createSendMessageToRoomEvent
import kotlinmud.event.impl.Event
import kotlinmud.event.impl.FightRoundEvent
import kotlinmud.event.impl.SendMessageToRoomEvent
import kotlinmud.event.observer.type.ObserverV2
import kotlinmud.io.factory.createDeathMessage
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.fight.Round
import org.jetbrains.exposed.sql.transactions.transaction

class EventService {
    var observersV2: ObserverV2 = mapOf()

    fun <T> publish(event: Event<T>) {
        (observersV2[event.eventType] ?: return).forEach { transaction { it(event) } }
    }

    fun publishRoomMessage(event: Event<SendMessageToRoomEvent>) {
        publish(event)
    }

    fun publishDeath(mob: MobDAO) {
        publish(createSendMessageToRoomEvent(createDeathMessage(mob), mob.room, mob))
    }

    fun publishFightRound(round: Round) {
        publish(FightRoundEvent(round))
    }
}

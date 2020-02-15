package kotlinmud.event.observer

import kotlinmud.attributes.AttributesEntity
import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.EventResponse
import kotlinmud.event.response.ClientConnectedResponse
import kotlinmud.item.InventoryEntity
import kotlinmud.mob.Disposition
import kotlinmud.mob.MobEntity
import org.jetbrains.exposed.sql.transactions.transaction

class ClientConnectedObserver: Observer {
    override val eventTypes: List<EventType> = listOf(EventType.CLIENT_CONNECTED)

    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        val mob = transaction {
            MobEntity.new {
                name = "a test mob"
                description = "a test mob is here, being a test."
                disposition = Disposition.STANDING.value
                inventory = InventoryEntity.new {}
                hp = 20
                mana = 100
                mv = 100
                attributes = AttributesEntity.new {}
            }
        }
        return ClientConnectedResponse(mob as A)
    }
}
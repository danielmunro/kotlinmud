package kotlinmud.event.observer

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.event.createSendMessageToRoomEvent
import kotlinmud.io.Message
import kotlinmud.io.TICK_LENGTH_IN_SECONDS
import kotlinmud.mob.JobType
import kotlinmud.mob.Mob
import kotlinmud.random.dN
import kotlinmud.service.EventService
import kotlinmud.service.MobService
import java.lang.IndexOutOfBoundsException
import java.util.*
import kotlin.concurrent.schedule

class ScavengerCollectsItemsObserver(
    private val mobService: MobService,
    private val eventService: EventService) : Observer {
    override val eventTypes: List<EventType> = listOf(EventType.TICK)

    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        mobService.getMobRooms().filter {
            it.mob.job == JobType.SCAVENGER
        }.forEach {
            eventually { pickUpItem(it.mob) }
        }
        @Suppress("UNCHECKED_CAST")
        return EventResponse(event as A)
    }

    private fun pickUpItem(mob: Mob) {
        val room = mobService.getRoomForMob(mob)
        if (mob.isStanding()) {
            try {
                val item = room.inventory.items.random()
                room.inventory.items.remove(item)
                mob.inventory.items.add(item)
                eventService.publishRoomMessage(createSendMessageToRoomEvent(
                    Message(
                        "you pick up $item.",
                        "$mob picks up $item."
                    ),
                    room,
                    mob
                ))
            } catch (e: IndexOutOfBoundsException) {
            }
        }
    }
}

fun eventually(doThis: TimerTask.() -> Unit) {
    Timer().schedule(((TICK_LENGTH_IN_SECONDS / Random().nextInt(2) + 1) * 1000).toLong(), doThis)
}

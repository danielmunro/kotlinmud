package kotlinmud.mob

import kotlinmud.event.createSendMessageToRoomEvent
import kotlinmud.io.Message
import kotlinmud.service.EventService
import kotlinmud.service.MobService

class MobController(private val mobService: MobService, private val eventService: EventService, private val mob: Mob) {
    fun wander() {
        val room = mobService.getRoomForMob(mob)
        val exit = room.exits.random()
        mobService.moveMob(mob, exit.destination, exit.direction)
    }

    fun pickUpAnyItem() {
        val room = mobService.getRoomForMob(mob)
        if (mob.isStanding() && room.inventory.items.size > 0) {
            val item = room.inventory.items.random()
            room.inventory.items.remove(item)
            mob.inventory.items.add(item)
            eventService.publishRoomMessage(
                createSendMessageToRoomEvent(
                    Message(
                        "you pick up $item.",
                        "$mob picks up $item."
                    ),
                    room,
                    mob
                ))
        }
    }
}

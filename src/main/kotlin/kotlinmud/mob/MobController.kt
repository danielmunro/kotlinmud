package kotlinmud.mob

import kotlinmud.event.createSendMessageToRoomEvent
import kotlinmud.io.Message
import kotlinmud.path.Pathfinder
import kotlinmud.service.EventService
import kotlinmud.service.MobService

class MobController(private val mobService: MobService, private val eventService: EventService, private val mob: Mob) {
    fun move() {
        when (mob.job) {
            JobType.FODDER, JobType.SCAVENGER -> wander()
            JobType.PATROL -> proceedRoute()
            else -> return
        }
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

    private fun proceedRoute() {
        val nextRoomId = mob.route[mob.lastRoute]
        val currentRoom = mobService.getRoomForMob(mob)
        val nextRoom = mobService.getRooms().find { it.id == nextRoomId }!!
        if (currentRoom.id == nextRoomId) {
            mob.lastRoute += 1
            if (mob.lastRoute == mob.route.size) {
                mob.lastRoute = 0
            }
            return proceedRoute()
        }
        println("mob $mob moving on route")
        val path = Pathfinder(currentRoom, nextRoom)
        val rooms = path.find()
        val nextMove = currentRoom.exits.find { it.destination == rooms[1] }!!
        mobService.moveMob(mob, nextMove.destination, nextMove.direction)
    }

    private fun wander() {
        println("mob $mob moving via random choice")
        val room = mobService.getRoomForMob(mob)
        val exit = room.exits.random()
        mobService.moveMob(mob, exit.destination, exit.direction)
    }
}

package kotlinmud.mob

import kotlinmud.event.createSendMessageToRoomEvent
import kotlinmud.io.Message
import kotlinmud.path.Pathfinder
import kotlinmud.service.EventService
import kotlinmud.service.ItemService
import kotlinmud.service.MobService
import kotlinmud.world.room.Room
import kotlinmud.world.room.exit.DoorDisposition
import kotlinmud.world.room.exit.Exit

class MobController(
    private val mobService: MobService,
    private val itemService: ItemService,
    private val eventService: EventService,
    private val mob: Mob
) {
    fun move() {
        when (mob.job) {
            JobType.FODDER, JobType.SCAVENGER -> wander()
            JobType.PATROL -> proceedRoute()
            else -> return
        }
    }

    fun pickUpAnyItem() {
        val room = mobService.getRoomForMob(mob)
        val items = itemService.findAllByOwner(room)
        if (mob.isStanding() && items.isNotEmpty()) {
            val item = items.random()
            itemService.changeItemOwner(item, mob)
            eventService.publishRoomMessage(
                createSendMessageToRoomEvent(
                    Message(
                        "you pick up $item.",
                        "$mob picks up $item."
                    ),
                    room,
                    mob
                )
            )
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
        if (openDoorIfExistsAndClosed(currentRoom, nextMove)) {
            mobService.moveMob(mob, nextMove.destination, nextMove.direction)
        }
    }

    private fun wander() {
        println("mob $mob moving via random choice")
        val room = mobService.getRoomForMob(mob)
        room.openExits().filter { it.destination.area == room.area }.random().let {
            mobService.moveMob(mob, it.destination, it.direction)
        }
    }

    private fun openDoorIfExistsAndClosed(room: Room, exit: Exit): Boolean {
        if (exit.door != null && exit.door.disposition == DoorDisposition.LOCKED) {
            return false
        } else if (exit.door != null && exit.door.disposition == DoorDisposition.CLOSED) {
            exit.door.disposition = DoorDisposition.OPEN
            eventService.publishRoomMessage(
                createSendMessageToRoomEvent(
                    Message(
                        "you open ${exit.door}.",
                        "$mob opens ${exit.door}."
                    ),
                    room,
                    mob
                )
            )
        }
        return true
    }
}

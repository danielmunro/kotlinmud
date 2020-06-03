package kotlinmud.mob.controller

import kotlinmud.event.EventService
import kotlinmud.event.createSendMessageToRoomEvent
import kotlinmud.io.MessageBuilder
import kotlinmud.item.ItemService
import kotlinmud.mob.model.Mob
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.JobType
import kotlinmud.path.Pathfinder
import kotlinmud.room.model.Exit
import kotlinmud.room.model.Room
import kotlinmud.room.type.DoorDisposition
import org.slf4j.LoggerFactory

class MobController(
    private val mobService: MobService,
    private val itemService: ItemService,
    private val eventService: EventService,
    private val mob: Mob
) {
    private val logger = LoggerFactory.getLogger(MobController::class.java)

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
            logger.debug("$mob picks up $item")
            itemService.changeItemOwner(item, mob)
            eventService.publishRoomMessage(
                createSendMessageToRoomEvent(
                    MessageBuilder()
                        .toActionCreator("you pick up $item.")
                        .toObservers("$mob picks up $item.")
                        .build(),
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
        logger.debug("mob $mob moving on route, index: ${mob.lastRoute}")
        val path = Pathfinder(currentRoom, nextRoom)
        val rooms = path.find()
        val nextMove = currentRoom.exits.find { it.destination == rooms[1] }!!
        if (openDoorIfExistsAndClosed(currentRoom, nextMove)) {
            mobService.moveMob(mob, nextMove.destination, nextMove.direction)
        }
    }

    private fun wander() {
        logger.debug("mob $mob moving via random choice")
        val room = mobService.getRoomForMob(mob)
        room.openExits().filter { it.destination.area == room.area }.random().let {
            mobService.moveMob(mob, it.destination, it.direction)
        }
    }

    private fun openDoorIfExistsAndClosed(room: Room, exit: Exit): Boolean {
        if (exit.door != null && exit.door.disposition == DoorDisposition.LOCKED) {
            return false
        } else if (exit.door != null && exit.door.disposition == DoorDisposition.CLOSED) {
            logger.debug("$mob opens ${exit.door} [${exit.direction}]")
            exit.door.disposition = DoorDisposition.OPEN
            eventService.publishRoomMessage(
                createSendMessageToRoomEvent(
                    MessageBuilder()
                        .toActionCreator("you open ${exit.door}.")
                        .toObservers("$mob opens ${exit.door}.")
                        .build(),
                    room,
                    mob
                )
            )
        }
        return true
    }
}

package kotlinmud.mob.controller

import kotlinmud.event.factory.createSendMessageToRoomEvent
import kotlinmud.event.service.EventService
import kotlinmud.helper.logger
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.model.Mob
import kotlinmud.mob.path.Pathfinder
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.JobType
import kotlinmud.room.model.Door
import kotlinmud.room.model.Room
import kotlinmud.room.type.DoorDisposition
import kotlinx.coroutines.runBlocking

class MobController(
    private val mobService: MobService,
    private val eventService: EventService,
    private val mob: Mob
) {
    private val logger = logger(this)

    fun move() {
        when (mob.job) {
            JobType.FODDER, JobType.SCAVENGER -> runBlocking { wander() }
            JobType.PATROL -> runBlocking { proceedRoute() }
            else -> return
        }
    }

    suspend fun pickUpAnyItem() {
        val room = mob.room
        val items = room.items.toList()
        if (mob.disposition == Disposition.STANDING && items.isNotEmpty()) {
            val item = items.random()
            mob.items.add(item)
            room.items.remove(item)
            eventService.publish(
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

    private suspend fun proceedRoute() {
        val currentRoom = mob.room
        val currentRoomIndex = mob.route!!.indexOf(currentRoom)
        val lastIndex = mob.route.indexOf(mob.lastRoute)
        val nextIndex = if (currentRoomIndex > lastIndex) {
            if (currentRoomIndex + 1 < mob.route.size) {
                currentRoomIndex + 1
            } else {
                currentRoomIndex - 1
            }
        } else {
            if (currentRoomIndex - 1 < 0) {
                1
            } else {
                currentRoomIndex - 1
            }
        }
        val nextRoom = mob.route[nextIndex]
        logger.debug("mob $mob moving on route, from $currentRoom to $nextRoom")
        val path = Pathfinder(currentRoom, nextRoom)
        val rooms = path.find()
        val nextMove = currentRoom.getAllExits().entries.find {
            it.value == rooms[1]
        }!!
        mob.lastRoute = mob.room
        val door = currentRoom.getDoors().entries.find {
            it.key == nextMove.key
        }?.value
        if (openDoorIfExistsAndClosed(currentRoom, door)) {
            mobService.moveMob(mob, nextMove.value, nextMove.key)
        }
    }

    private suspend fun wander() {
        logger.debug("mob $mob moving via random choice")
        val room = mob.room
        room.getAllExits().filter { it.value.area == room.area }.entries.random().let {
            mobService.moveMob(mob, it.value, it.key)
        }
    }

    private suspend fun openDoorIfExistsAndClosed(room: Room, door: Door?): Boolean {
        if (door != null && door.disposition == DoorDisposition.LOCKED) {
            return false
        } else if (door != null && door.disposition == DoorDisposition.CLOSED) {
            logger.debug("$mob opens $door")
            door.disposition = DoorDisposition.OPEN
            eventService.publish(
                createSendMessageToRoomEvent(
                    MessageBuilder()
                        .toActionCreator("you open $door.")
                        .toObservers("$mob opens $door.")
                        .build(),
                    room,
                    mob
                )
            )
        }
        return true
    }
}

package kotlinmud.mob.controller

import kotlinmud.event.factory.createSendMessageToRoomEvent
import kotlinmud.event.service.EventService
import kotlinmud.helper.logger
import kotlinmud.io.model.MessageBuilder
import kotlinmud.item.service.ItemService
import kotlinmud.mob.model.Mob
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.JobType
import kotlinmud.room.dao.DoorDAO
import kotlinmud.room.model.Room
import kotlinmud.room.type.DoorDisposition
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.transaction

class MobController(
    private val mobService: MobService,
    private val itemService: ItemService,
    private val eventService: EventService,
    private val mob: Mob
) {
    private val logger = logger(this)

    fun move() {
        when (mob.job) {
            JobType.FODDER, JobType.SCAVENGER -> runBlocking { wander() }
            JobType.PATROL -> transaction { runBlocking { proceedRoute() } }
            else -> return
        }
    }

    suspend fun pickUpAnyItem() {
        println("debug sanity -- pickUpAnyItem")
        val room = transaction { mob.room }
        val items = room.items.toList()
        println("pre-conditional")
        if (mob.disposition == Disposition.STANDING && items.isNotEmpty()) {
            println("inside conditional")
            val item = items.random()
            logger.debug("$mob picks up $item")
//            mob.items.add(item)
            println("pre-event publish")
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
            println("woot")
        }
    }

    private suspend fun proceedRoute() {
//        if (mob.lastRoute == null) {
//            mob.lastRoute = 0
//        }
//        val nextRoomId = mob.route?.get(mob.lastRoute!!)!!
//        val currentRoom = mob.room
//        val nextRoom = findRoomById(nextRoomId)
//        val currentRoomId = currentRoom.id.value
//        if (currentRoomId == nextRoomId) {
//            mob.lastRoute = mob.lastRoute?.plus(1)
//            if (mob.lastRoute == mob.route?.size) {
//                mob.lastRoute = 0
//            }
//
//            return proceedRoute()
//        }
//        logger.debug("mob $mob moving on route, index: ${mob.lastRoute}")
//        val path = Pathfinder(currentRoom, nextRoom)
//        val rooms = path.find()
//        val nextMove = currentRoom.getAllExits().entries.find {
//            it.value == rooms[1]
//        }!!
//        val door = currentRoom.getDoors().entries.find {
//            it.key == nextMove.key
//        }?.value
//        if (openDoorIfExistsAndClosed(currentRoom, door)) {
//            mobService.moveMob(mob, nextMove.value, nextMove.key)
//        }
    }

    private suspend fun wander() {
        logger.debug("mob $mob moving via random choice")
        val room = mob.room
        room.getAllExits().filter { it.value.area == room.area }.entries.random().let {
            mobService.moveMob(mob, it.value, it.key)
        }
    }

    private suspend fun openDoorIfExistsAndClosed(room: Room, door: DoorDAO?): Boolean {
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

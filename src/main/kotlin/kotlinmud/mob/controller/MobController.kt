package kotlinmud.mob.controller

import kotlinmud.event.factory.createSendMessageToRoomEvent
import kotlinmud.event.service.EventService
import kotlinmud.helper.logger
import kotlinmud.io.model.MessageBuilder
import kotlinmud.item.service.ItemService
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.JobType
import kotlinmud.path.Pathfinder
import kotlinmud.room.dao.DoorDAO
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.type.DoorDisposition

class MobController(
    private val mobService: MobService,
    private val itemService: ItemService,
    private val eventService: EventService,
    private val mob: MobDAO
) {
    private val logger = logger(this)

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
            itemService.giveItemToMob(item, mob)
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
        val nextRoomId = mob.route?.get(mob.lastRoute!!)!!
        val currentRoom = mobService.getRoomForMob(mob)
        val nextRoom = mobService.getRoomById(nextRoomId)!!
        if (currentRoom.id.value == nextRoomId) {
            mob.lastRoute = mob.lastRoute?.plus(1)
            if (mob.lastRoute == mob.route?.size) {
                mob.lastRoute = 0
            }
            return proceedRoute()
        }
        logger.debug("mob $mob moving on route, index: ${mob.lastRoute}")
        val path = Pathfinder(currentRoom, nextRoom)
        val rooms = path.find()
        val nextMove = currentRoom.getAllExits().entries.find { it.value == rooms[1] }!!
        val door = currentRoom.getDoors().entries.find { it.key == nextMove.key }!!.value
        if (openDoorIfExistsAndClosed(currentRoom, door)) {
            mobService.moveMob(mob, nextMove.value, nextMove.key)
        }
    }

    private fun wander() {
        logger.debug("mob $mob moving via random choice")
        val room = mobService.getRoomForMob(mob)
        room.getAllExits().filter { it.value.area == room.area }.entries.random().let {
            mobService.moveMob(mob, it.value, it.key)
        }
    }

    private fun openDoorIfExistsAndClosed(room: RoomDAO, door: DoorDAO?): Boolean {
        if (door != null && door.disposition == DoorDisposition.LOCKED) {
            return false
        } else if (door != null && door.disposition == DoorDisposition.CLOSED) {
            logger.debug("$mob opens $door")
            door.disposition = DoorDisposition.OPEN
            eventService.publishRoomMessage(
                createSendMessageToRoomEvent(
                    MessageBuilder()
                        .toActionCreator("you open ${door}.")
                        .toObservers("$mob opens ${door}.")
                        .build(),
                    room,
                    mob
                )
            )
        }
        return true
    }
}

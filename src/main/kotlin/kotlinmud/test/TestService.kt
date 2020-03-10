package kotlinmud.test

import kotlinmud.attributes.Attributes
import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.io.IOStatus
import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.io.Server
import kotlinmud.item.Inventory
import kotlinmud.item.Item
import kotlinmud.mob.JobType
import kotlinmud.mob.Mob
import kotlinmud.mob.MobRoom
import kotlinmud.mob.SpecializationType
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.fight.Round
import kotlinmud.mob.race.impl.Human
import kotlinmud.room.Room
import kotlinmud.service.*

class TestService(
    private val fixtureService: FixtureService,
    private val mobService: MobService,
    private val actionService: ActionService,
    private val respawnService: RespawnService,
    private val eventService: EventService,
    val server: Server
) {
    init {
        fixtureService.createItem(mobService.getStartRoom().inventory)
    }

    fun <T, A> publish(event: Event<T>): EventResponse<A> {
        return eventService.publish(event)
    }

    fun respawnWorld() {
        respawnService.respawn()
    }

    fun getStartRoom(): Room {
        return mobService.getStartRoom()
    }

    fun createMob(job: JobType = JobType.NONE): Mob {
        val mob = fixtureService.createMob(Human(), SpecializationType.NONE, job)
        mobService.addMob(mob)
        return mob
    }

    fun createItem(inventory: Inventory, attributes: Attributes = Attributes()): Item {
        return fixtureService.createItem(inventory, attributes)
    }

    fun getMobRooms(): List<MobRoom> {
        return mobService.getMobRooms()
    }

    fun getRoomForMob(mob: Mob): Room {
        return mobService.getRoomForMob(mob)
    }

    fun getMobsForRoom(room: Room): List<Mob> {
        return mobService.getMobsForRoom(room)
    }

    fun pruneDeadMobs() {
        mobService.pruneDeadMobs()
    }

    fun decrementDelays() {
        mobService.decrementDelays()
    }

    fun decrementAffects() {
        mobService.decrementAffects()
    }

    fun runAction(mob: Mob, input: String): Response {
        return actionService.run(Request(mob, input, mobService.getRoomForMob(mob)))
    }

    fun runActionForIOStatus(mob: Mob, input: String, status: IOStatus): Response {
        var i = 0
        while (i < 100) {
            val response = runAction(mob, input)
            if (response.status == status) {
                return response
            }
            i++
        }
        throw Exception("cannot generate desired IOStatus")
    }

    fun addFight(fight: Fight) {
        mobService.addFight(fight)
    }

    fun findFightForMob(mob: Mob): Fight? {
        return mobService.findFightForMob(mob)
    }

    fun proceedFights(): List<Round> {
        return mobService.proceedFights()
    }
}

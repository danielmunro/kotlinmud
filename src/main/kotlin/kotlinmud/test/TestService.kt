package kotlinmud.test

import kotlinmud.attributes.Attributes
import kotlinmud.io.IOStatus
import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.item.Inventory
import kotlinmud.item.Item
import kotlinmud.mob.Mob
import kotlinmud.mob.MobRoom
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.fight.Round
import kotlinmud.room.Room
import kotlinmud.service.ActionService
import kotlinmud.service.FixtureService
import kotlinmud.service.MobService
import kotlinmud.service.RespawnService

class TestService(
    private val fixtureService: FixtureService,
    private val mobService: MobService,
    private val actionService: ActionService,
    private val respawnService: RespawnService
) {
    init {
        fixtureService.createItem(mobService.getStartRoom().inventory)
    }

    fun respawnWorld() {
        respawnService.respawn()
    }

    fun getStartRoom(): Room {
        return mobService.getStartRoom()
    }

    fun createMob(): Mob {
        val mob = fixtureService.createMob()
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

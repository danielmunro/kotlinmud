package kotlinmud.test

import kotlinmud.attributes.Attributes
import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.item.Inventory
import kotlinmud.item.Item
import kotlinmud.mob.Mob
import kotlinmud.room.Room
import kotlinmud.service.ActionService
import kotlinmud.service.FixtureService
import kotlinmud.service.MobService

class TestService(
    private val fixtureService: FixtureService,
    private val mobService: MobService,
    private val actionService: ActionService
) {

    fun createMob(): Mob {
        val mob = fixtureService.createMob()
        mobService.respawnMobToStartRoom(mob)
        return mob
    }

    fun createItem(inventory: Inventory, attributes: Attributes = Attributes()): Item {
        return fixtureService.createItem(inventory, attributes)
    }

    fun getRoomForMob(mob: Mob): Room {
        return mobService.getRoomForMob(mob)
    }

    fun getMobsForRoom(room: Room): List<Mob> {
        return mobService.getMobsForRoom(room)
    }

    fun runAction(mob: Mob, input: String): Response {
        return actionService.run(Request(mob, input, mobService.getRoomForMob(mob)))
    }
}

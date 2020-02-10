package kotlinmud.test

import kotlinmud.ActionService
import kotlinmud.MobService
import kotlinmud.fixture.FixtureService
import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.item.InventoryEntity
import kotlinmud.item.ItemEntity
import kotlinmud.mob.MobEntity
import kotlinmud.room.RoomEntity

class TestService(
    private val fixtureService: FixtureService,
    private val mobService: MobService,
    private val actionService: ActionService) {

    fun createMob(): MobEntity {
        val mob = fixtureService.createMob()
        mobService.respawnMobToStartRoom(mob)
        return mob
    }

    fun createItem(inventory: InventoryEntity): ItemEntity {
        return fixtureService.createItem(inventory)
    }

    fun getRoomForMob(mob: MobEntity): RoomEntity {
        return mobService.getRoomForMob(mob)
    }

    fun getMobsForRoom(room: RoomEntity): List<MobEntity> {
        return mobService.getMobsForRoom(room)
    }

    fun runAction(mob: MobEntity, input: String): Response {
        return actionService.run(Request(mob, input, mobService.getRoomForMob(mob)))
    }
}
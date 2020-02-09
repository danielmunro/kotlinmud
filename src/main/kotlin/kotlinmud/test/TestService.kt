package kotlinmud.test

import kotlinmud.ActionService
import kotlinmud.MobService
import kotlinmud.fixture.FixtureService
import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.mob.Mob
import kotlinmud.room.Room

class TestService(
    private val fixtureService: FixtureService,
    private val mobService: MobService,
    private val actionService: ActionService) {

    fun createMob(): Mob {
        val mob = fixtureService.createMob()
        mobService.respawnMobToStartRoom(mob)
        return mob
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
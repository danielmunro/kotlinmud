package kotlinmud.test

import kotlinmud.ActionService
import kotlinmud.EventService
import kotlinmud.MobService
import kotlinmud.db.applyDBSchema
import kotlinmud.db.connect
import kotlinmud.db.disconnect
import kotlinmud.event.observer.createObservers
import kotlinmud.fixture.FixtureService

fun createTestService(): TestService {
    val fixtureService = FixtureService()
    val mobService = MobService(fixtureService.generateWorld())
    val actionService = ActionService(mobService, EventService(createObservers(mobService)))
    return TestService(fixtureService, mobService, actionService)
}

fun globalSetup() {
    connect()
    applyDBSchema()
}

fun globalTeardown() {
    disconnect()
}

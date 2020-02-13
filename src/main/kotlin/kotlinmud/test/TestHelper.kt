package kotlinmud.test

import kotlinmud.db.applyDBSchema
import kotlinmud.db.connect
import kotlinmud.db.disconnect
import kotlinmud.event.observer.createObservers
import kotlinmud.service.ActionService
import kotlinmud.service.EventService
import kotlinmud.service.FixtureService
import kotlinmud.service.MobService

fun createTestService(): TestService {
    val fixtureService = FixtureService()
    val mobService = MobService(fixtureService.generateWorld())
    val actionService =
        ActionService(mobService, EventService(createObservers(mobService)))
    return TestService(fixtureService, mobService, actionService)
}

fun globalSetup() {
    connect()
    applyDBSchema()
}

fun globalTeardown() {
    disconnect()
}

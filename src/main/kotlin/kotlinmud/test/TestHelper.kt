package kotlinmud.test

import kotlinmud.db.applyDBSchema
import kotlinmud.db.connect
import kotlinmud.db.disconnect
import kotlinmud.event.observer.createObservers
import kotlinmud.io.Server
import kotlinmud.service.ActionService
import kotlinmud.service.EventService
import kotlinmud.service.FixtureService
import kotlinmud.service.MobService
import java.net.ServerSocket

fun createTestService(): TestService {
    val fixtureService = FixtureService()
    val mobService = MobService(fixtureService.generateWorld())
    val eventService = EventService()
    val actionService = ActionService(mobService, eventService)
    eventService.observers = createObservers(Server(eventService, mobService, ServerSocket()), mobService)
    return TestService(fixtureService, mobService, actionService)
}

fun globalSetup() {
    connect()
    applyDBSchema()
}

fun globalTeardown() {
    disconnect()
}

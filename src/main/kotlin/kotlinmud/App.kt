package kotlinmud

import java.net.ServerSocket
import kotlinmud.db.applyDBSchema
import kotlinmud.db.connect
import kotlinmud.event.observer.createObservers
import kotlinmud.io.Server
import kotlinmud.service.ActionService
import kotlinmud.service.EventService
import kotlinmud.service.FixtureService
import kotlinmud.service.MobService

class App(eventService: EventService, mobService: MobService, private val server: Server) {
    private val actionService: ActionService = ActionService(mobService, eventService)

    fun start() {
        println("starting app")
        server.start()
        processClientBuffers()
    }

    private fun processClientBuffers() {
        while (true) {
            server.getClientsWithBuffers().forEach {
                val output = actionService.run(it.shiftBuffer())
                it.write("${output.message.toActionCreator}\n---> ")
            }
        }
    }
}

fun main() {
    connect()
    applyDBSchema()
    val fixtureService = FixtureService()
    val mobService = MobService(fixtureService.generateWorld())
    fixtureService.populateWorld(mobService)
    val eventService = EventService(createObservers(mobService))
    App(eventService, mobService, Server(eventService, mobService, ServerSocket(9999))).start()
}

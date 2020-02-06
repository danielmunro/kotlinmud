package kotlinmud

import kotlinmud.db.connect
import kotlinmud.db.applyDBSchema
import kotlinmud.event.observer.createObservers
import kotlinmud.fixture.FixtureService
import kotlinmud.io.Server
import java.net.ServerSocket

class App(private val mobService: MobService, private val server: Server) {
    private val eventService: EventService = EventService(createObservers())
    private val actionService: ActionService = ActionService(eventService)
    private val fixtureService: FixtureService = FixtureService()

    fun start() {
        println("starting app")
        server.start()
        processClientBuffers()
    }

    private fun processClientBuffers() {
        while (true) {
            server.getClientsWithBuffers().forEach {
                val output = actionService.run(it.shiftBuffer())
                println("output: ${output.message}")
                it.write(output.message)
            }
        }
    }
}

fun main() {
    connect()
    applyDBSchema()
    val mobService = MobService()
    App(mobService, Server(mobService, ServerSocket(9999))).start()
}

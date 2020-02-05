package kotlinmud

import kotlinmud.db.connect
import kotlinmud.db.applyDBSchema
import kotlinmud.fixture.FixtureService
import kotlinmud.io.Server
import java.net.ServerSocket

class App(private val server: Server) {
    private val actionService: ActionService = ActionService()
    private val mobService: MobService = MobService()
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
    App(Server(ServerSocket(9999))).start()
}

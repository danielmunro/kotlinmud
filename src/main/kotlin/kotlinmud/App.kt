package kotlinmud

import kotlinmud.exit.Exits
import kotlinmud.fixture.FixtureService
import kotlinmud.io.Server
import kotlinmud.mob.Mobs
import kotlinmud.room.Rooms
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import java.net.ServerSocket

class App(private val server: Server) {
    private val actionService: ActionService = ActionService()
    private val fixtureService: FixtureService = FixtureService()

    fun start() {
        server.start()
        processClientBuffers()
    }

    private fun processClientBuffers() {
        println("processing app client buffers")
        while (true) {
            println("clients: ${server.getClientCount()}")
            server.getClientsWithBuffers().forEach {
                println("debug 1")
                val input = it.buffer.removeAt(0)
                println("pop off: $input")
                println(it.buffer)
            }
            Thread.sleep(1000)
        }
    }
}

fun main() {
    Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver", user = "root", password = "")
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(Mobs, Rooms, Exits)
    }
    val app = App(Server(ServerSocket(9999)))
    app.start()
}

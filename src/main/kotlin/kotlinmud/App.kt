package kotlinmud

import kotlinmud.app.createApp
import kotlinmud.db.createConnection
import kotlinx.coroutines.runBlocking

fun main() {
    val env = System.getenv("ENV") ?: "dev"
    val port = if (env == "ci") 0 else 9999
    createConnection()
    with(createApp(port)) {
        runBlocking { startGame() }
        while (isRunning()) {
            runBlocking { loop() }
        }
    }
}

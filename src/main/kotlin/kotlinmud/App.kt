package kotlinmud

import kotlinmud.db.createConnection
import kotlinmud.world.parser.Parser

fun main() {
    val env = System.getenv("ENV") ?: "dev"
    val port = if (env == "ci") 0 else 9999
    createConnection()
    parseWorld()
//    with(createApp(port)) {
//        runBlocking { startGame() }
//        while (isRunning()) {
//            runBlocking { loop() }
//        }
//    }
}

fun parseWorld() {
    val parser = Parser("./world/lorimir-forest.txt")
    print(parser.parseFile())
}

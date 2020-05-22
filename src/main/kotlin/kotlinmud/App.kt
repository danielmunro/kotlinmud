package kotlinmud

import kotlinmud.app.createApp

fun main() {
    val env = System.getenv("ENV") ?: "dev"
    val port = if (env == "ci") 0 else 9999
    System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug")
    createApp(port).start()
}

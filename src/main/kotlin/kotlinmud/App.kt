package kotlinmud

import kotlinmud.app.createApp

fun main() {
    val env = System.getenv("ENV")
    println("env: $env")
    val port = if (env == "ci") 0 else 9999
    createApp(port).start()
}

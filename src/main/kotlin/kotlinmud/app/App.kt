package kotlinmud.app

import kotlinmud.event.factory.createGameLoopEvent
import kotlinmud.event.factory.createGameStartEvent
import kotlinmud.event.factory.createTickEvent
import kotlinmud.event.service.EventService
import kotlinmud.helper.logger
import kotlinmud.io.service.ServerService

class App(private val eventService: EventService, private val serverService: ServerService) {
    private val logger = logger(this)
    private var running = true

    suspend fun startGame() {
        logger.info("starting app on port ${serverService.port}")
        eventService.publish(createGameStartEvent())
        eventService.publish(createTickEvent())
    }

    suspend fun loop() {
        eventService.publish(createGameLoopEvent())
        if (!serverService.isConnected()) {
            running = false
        }
    }

    fun isRunning(): Boolean {
        return running
    }

    fun stop() {
        running = false
    }
}

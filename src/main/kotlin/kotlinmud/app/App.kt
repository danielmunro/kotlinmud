package kotlinmud.app

import kotlinmud.event.createSendMessageToRoomEvent
import kotlinmud.io.NIOClient
import kotlinmud.io.NIOServer
import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.mob.Mob
import kotlinmud.service.ActionService
import kotlinmud.service.EventService
import kotlinmud.service.MobService
import kotlinmud.service.TimeService

class App(
    private val eventService: EventService,
    private val mobService: MobService,
    private val timeService: TimeService,
    private val server: NIOServer,
    private val actionService: ActionService
) {
    fun start() {
        println("starting app on port ${server.port}")
        server.configure()
        mainLoop()
    }

    private fun mainLoop() {
        println("starting main loop")
        while (true) {
            server.readIntoBuffers()
            processClientBuffers()
            server.removeDisconnectedClients()
            timeService.loop()
        }
    }

    private fun processClientBuffers() {
        server.getClientsWithBuffers().forEach {
            processRequest(it)
        }
    }

    private fun processRequest(client: NIOClient) {
        if (client.mob!!.delay > 0) {
            return
        }
        val input = client.buffers.removeAt(0)
        val request = Request(client.mob!!, input, mobService.getRoomForMob(client.mob!!))
        val response = actionService.run(request)
        eventService.publishRoomMessage(
            createSendMessageToRoomEvent(
                response.message,
                mobService.getRoomForMob(request.mob),
                request.mob,
                getTarget(response)
            )
        )
    }

    private fun getTarget(response: Response): Mob? {
        return try {
            response.actionContextList.getResultBySyntax<Mob>(Syntax.MOB_IN_ROOM)
        } catch (e: Exception) {
            null
        }
    }
}

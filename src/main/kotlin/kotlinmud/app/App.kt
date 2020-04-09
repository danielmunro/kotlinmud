package kotlinmud.app

import kotlinmud.event.createSendMessageToRoomEvent
import kotlinmud.io.ClientHandler
import kotlinmud.io.Response
import kotlinmud.io.Server
import kotlinmud.io.Syntax
import kotlinmud.mob.Mob
import kotlinmud.service.ActionService
import kotlinmud.service.EventService
import kotlinmud.service.ItemService
import kotlinmud.service.MobService

class App(
    private val eventService: EventService,
    private val mobService: MobService,
    itemService: ItemService,
    private val server: Server
) {
    private val actionService: ActionService =
        ActionService(mobService, itemService, eventService, server)

    fun start() {
        println("starting app on port ${server.getPort()}")
        server.start()
        processClientBuffers()
    }

    private fun processClientBuffers() {
        while (true) {
            server.getClientsWithBuffers().forEach {
                processRequest(it)
            }
            server.pruneClients()
        }
    }

    private fun processRequest(client: ClientHandler) {
        if (client.mob.delay > 0) {
            return
        }
        val request = client.shiftBuffer()
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

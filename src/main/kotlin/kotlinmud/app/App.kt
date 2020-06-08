package kotlinmud.app

import kotlinmud.action.service.ActionService
import kotlinmud.event.Event
import kotlinmud.event.EventService
import kotlinmud.event.EventType
import kotlinmud.event.createSendMessageToRoomEvent
import kotlinmud.event.event.DayEvent
import kotlinmud.io.model.Client
import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.model.Request
import kotlinmud.io.model.Response
import kotlinmud.io.service.ServerService
import kotlinmud.io.type.Syntax
import kotlinmud.mob.model.Mob
import kotlinmud.mob.service.MobService
import kotlinmud.player.service.PlayerService
import kotlinmud.service.TimeService
import org.slf4j.LoggerFactory

class App(
    private val eventService: EventService,
    private val mobService: MobService,
    private val timeService: TimeService,
    private val serverService: ServerService,
    private val actionService: ActionService,
    private val playerService: PlayerService
) {
    private val logger = LoggerFactory.getLogger(App::class.java)

    fun start() {
        logger.info("starting app on port ${serverService.port}")
        eventService.publish(Event(EventType.DAY, DayEvent()))
        mainLoop()
    }

    private fun mainLoop() {
        logger.info("starting main loop")
        while (true) {
            serverService.readIntoBuffers()
            processClientBuffers()
            serverService.removeDisconnectedClients()
            timeService.loop()
        }
    }

    private fun processClientBuffers() {
        serverService.getClientsWithBuffers().forEach {
            processRequest(it)
        }
    }

    private fun processRequest(client: Client) {
        if (client.delay > 0) {
            return
        }
        val input = client.buffers.removeAt(0)
        if (client.mob == null) {
            logger.debug("pre-auth request :: {} : {}", client.socket.remoteAddress, input)
            playerService.handlePreAuthRequest(PreAuthRequest(client, input))
            return
        }
        val request =
            Request(client.mob!!, input, mobService.getRoomForMob(client.mob!!))
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

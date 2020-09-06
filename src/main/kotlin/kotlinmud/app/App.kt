package kotlinmud.app

import kotlinmud.action.service.ActionService
import kotlinmud.db.createConnection
import kotlinmud.event.factory.createGameStartEvent
import kotlinmud.event.factory.createSendMessageToRoomEvent
import kotlinmud.event.service.EventService
import kotlinmud.helper.logger
import kotlinmud.io.model.Client
import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.model.Response
import kotlinmud.io.service.RequestService
import kotlinmud.io.service.ServerService
import kotlinmud.io.type.Syntax
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.service.MobService
import kotlinmud.player.service.PlayerService
import kotlinmud.time.service.TimeService
import org.jetbrains.exposed.sql.transactions.transaction

class App(
    private val eventService: EventService,
    private val timeService: TimeService,
    private val serverService: ServerService,
    private val actionService: ActionService,
    private val playerService: PlayerService,
    private val mobService: MobService
) {
    private val logger = logger(this)

    fun start() {
        logger.info("starting app on port ${serverService.port}")
        createConnection()
        eventService.publish(createGameStartEvent())
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
        val request = RequestService(client.mob!!, client.mob!!.id.value, mobService, input, transaction { client.mob!!.room })
        val response = actionService.run(request)
        eventService.publishRoomMessage(
            createSendMessageToRoomEvent(
                response.message,
                transaction { request.mob.room },
                request.mob,
                getTarget(response)
            )
        )
    }

    private fun getTarget(response: Response): MobDAO? {
        return try {
            response.actionContextList.getResultBySyntax<MobDAO>(Syntax.MOB_IN_ROOM)
        } catch (e: Exception) {
            null
        }
    }
}

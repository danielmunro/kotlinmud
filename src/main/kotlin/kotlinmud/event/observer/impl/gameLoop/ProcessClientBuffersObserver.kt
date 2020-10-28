package kotlinmud.event.observer.impl.gameLoop

import kotlinmud.action.service.ActionService
import kotlinmud.event.factory.createSendMessageToRoomEvent
import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
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
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import org.jetbrains.exposed.sql.transactions.transaction

class ProcessClientBuffersObserver(
    private val serverService: ServerService,
    private val playerService: PlayerService,
    private val mobService: MobService,
    private val actionService: ActionService,
    private val eventService: EventService
) : Observer {
    private val logger = logger(this)

    override suspend fun <T> invokeAsync(event: Event<T>) {
        serverService.getClientsWithBuffers().asFlow().collect {
            processRequest(it)
        }
    }
    private suspend fun processRequest(client: Client) {
        if (client.delay > 0) {
            return
        }
        val input = client.buffers.removeAt(0)
        if (client.mob == null) {
            logger.debug("pre-auth request :: {} : {}", client.socket.remoteAddress, input)
            playerService.handlePreAuthRequest(PreAuthRequest(client, input))
            return
        }
        val request = RequestService(client.mob!!.id.value, mobService, input)
        val response = actionService.run(request)
        eventService.publishRoomMessage(
            createSendMessageToRoomEvent(
                response.message,
                transaction { request.getMob().room },
                request.getMob(),
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

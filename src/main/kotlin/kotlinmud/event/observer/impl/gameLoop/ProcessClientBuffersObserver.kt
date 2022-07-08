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
import kotlinmud.mob.model.Mob
import kotlinmud.mob.model.PlayerMob
import kotlinmud.player.auth.impl.CompleteAuthStep
import kotlinmud.player.service.PlayerService
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect

class ProcessClientBuffersObserver(
    private val serverService: ServerService,
    private val playerService: PlayerService,
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
        if (client.isDelayed()) {
            return
        }

        client.shiftInput().also {
            if (client.isInGame()) {
                handleRequest(client.mob!!, it)
                return
            }

            handlePreAuthRequest(client, it)
        }
    }

    private suspend fun handleRequest(mob: PlayerMob, input: String) {
        val request = RequestService(mob, input)
        val response = actionService.run(request)
        eventService.publish(
            createSendMessageToRoomEvent(
                response.message,
                request.mob.room,
                request.mob,
                getTarget(response)
            )
        )
    }

    private suspend fun handlePreAuthRequest(client: Client, input: String) {
        val nextStep = playerService.handlePreAuthRequest(PreAuthRequest(client, input))
        client.write(nextStep.message + "\n")
        client.write(nextStep.authStep.promptMessage + " ")
        if (nextStep.authStep is CompleteAuthStep) {
            val response = actionService.run(RequestService(client.mob!!, "look"))
            client.writePrompt(response.message.toActionCreator)
        }
    }

    private fun getTarget(response: Response): Mob? {
        return try {
            response.actionContextList.getResultBySyntax<Mob>(Syntax.MOB_IN_ROOM)
        } catch (e: Exception) {
            null
        }
    }
}

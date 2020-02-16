package kotlinmud

import java.net.ServerSocket
import kotlinmud.db.applyDBSchema
import kotlinmud.db.connect
import kotlinmud.event.createSendMessageToRoomEvent
import kotlinmud.event.event.SendMessageToRoomEvent
import kotlinmud.event.observer.createObservers
import kotlinmud.event.response.SendMessageToRoomResponse
import kotlinmud.io.*
import kotlinmud.mob.MobEntity
import kotlinmud.service.ActionService
import kotlinmud.service.EventService
import kotlinmud.service.FixtureService
import kotlinmud.service.MobService
import java.lang.Exception

class App(private val eventService: EventService, private val mobService: MobService, private val server: Server) {
    private val actionService: ActionService = ActionService(mobService, eventService)

    fun start() {
        println("starting app")
        server.start()
        processClientBuffers()
    }

    private fun processClientBuffers() {
        while (true) {
            server.getClientsWithBuffers().forEach {  processRequest(it) }
        }
    }

    private fun processRequest(client: ClientHandler) {
        val request = client.shiftBuffer()
        val response = actionService.run(request)
        eventService.publish<SendMessageToRoomEvent, SendMessageToRoomResponse<SendMessageToRoomEvent>>(
            createSendMessageToRoomEvent(response.message, request.room, request.mob, getTarget(response)))
        client.write("\n---> ")
    }

    private fun getTarget(response: Response): MobEntity? {
        return try {
            response.actionContextList.getResultBySyntax<MobEntity>(Syntax.MOB_IN_ROOM)
        } catch (e: Exception) {
            null
        }
    }
}

fun main() {
    connect()
    applyDBSchema()
    val fixtureService = FixtureService()
    val mobService = MobService(fixtureService.generateWorld())
    fixtureService.populateWorld(mobService)
    val eventService = EventService()
    val server = Server(eventService, mobService, ServerSocket(9999))
    val observers = createObservers(server, mobService)
    eventService.observers = observers
    App(eventService, mobService, server).start()
}

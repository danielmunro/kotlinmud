package kotlinmud

import java.net.ServerSocket
import kotlinmud.db.applyDBSchema
import kotlinmud.db.connect
import kotlinmud.event.observer.createObservers
import kotlinmud.io.*
import kotlinmud.mob.MobEntity
import kotlinmud.service.ActionService
import kotlinmud.service.EventService
import kotlinmud.service.FixtureService
import kotlinmud.service.MobService
import java.lang.Exception

class App(eventService: EventService, private val mobService: MobService, private val server: Server) {
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
        val mobsInRoom = mobService.getMobsForRoom(request.room)
        sendMessageToMobsInRoom(mobsInRoom, request.mob, getTarget(response), response.message)
        client.write("\n---> ")
    }

    private fun getTarget(response: Response): MobEntity? {
        return try {
            response.actionContextList.getResultBySyntax<MobEntity>(Syntax.MOB_IN_ROOM)
        } catch (e: Exception) {
            null
        }
    }

    private fun sendMessageToMobsInRoom(mobs: List<MobEntity>, actionCreator: MobEntity, target: MobEntity?, message: Message) {
        var clients = server.getClientsFromMobs(mobs)
        clients.forEach {
            when(it.mob) {
                actionCreator -> it.write(message.toActionCreator)
                target -> it.write(message.toTarget)
                else -> it.write(message.toObservers)
            }
        }
    }
}

fun main() {
    connect()
    applyDBSchema()
    val fixtureService = FixtureService()
    val mobService = MobService(fixtureService.generateWorld())
    fixtureService.populateWorld(mobService)
    val eventService = EventService(createObservers(mobService))
    App(eventService, mobService, Server(eventService, mobService, ServerSocket(9999))).start()
}

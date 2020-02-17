package kotlinmud

import java.lang.Exception
import java.net.ServerSocket
import kotlinmud.db.applyDBSchema
import kotlinmud.db.connect
import kotlinmud.event.EventResponse
import kotlinmud.event.createSendMessageToRoomEvent
import kotlinmud.event.event.SendMessageToRoomEvent
import kotlinmud.event.observer.createObservers
import kotlinmud.io.*
import kotlinmud.mob.MobEntity
import kotlinmud.service.ActionService
import kotlinmud.service.EventService
import kotlinmud.service.FixtureService
import kotlinmud.service.MobService
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

class App(private val eventService: EventService, private val mobService: MobService, private val server: Server) {
    private val actionService: ActionService = ActionService(mobService, eventService)

    fun start() {
        println("starting app")
        server.start()
        processClientBuffers()
    }

    private fun processClientBuffers() {
        while (true) {
            server.getClientsWithBuffers().forEach { processRequest(it) }
        }
    }

    private fun processRequest(client: ClientHandler) {
        val request = client.shiftBuffer()
        val response = actionService.run(request)
        eventService.publish<SendMessageToRoomEvent, EventResponse<SendMessageToRoomEvent>>(
            createSendMessageToRoomEvent(response.message, mobService.getRoomForMob(request.mob), request.mob, getTarget(response)))
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
    val container = createContainer()
    val mobService: MobService by container.instance()
    val eventService: EventService by container.instance()
    val server: Server by container.instance()
    eventService.observers = createObservers(server, mobService)
    App(eventService, mobService, server).start()
}

fun createContainer(): Kodein {
    return Kodein {
        bind<ServerSocket>() with singleton { ServerSocket(9999) }
        bind<Server>() with singleton { Server(instance<EventService>(), instance<ServerSocket>()) }
        bind<FixtureService>() with singleton { FixtureService() }
        bind<EventService>() with singleton { EventService() }
        bind<ActionService>() with singleton { ActionService(instance<MobService>(), instance<EventService>()) }
        bind<MobService>() with singleton {
            val fix = instance<FixtureService>()
            val svc = MobService(fix.generateWorld())
            fix.populateWorld(svc)
            svc
        }
    }
}

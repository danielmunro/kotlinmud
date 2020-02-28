package kotlinmud

import java.io.File
import java.net.ServerSocket
import kotlinmud.db.applyDBSchema
import kotlinmud.db.connect
import kotlinmud.event.createSendMessageToRoomEvent
import kotlinmud.event.observer.createObservers
import kotlinmud.io.ClientHandler
import kotlinmud.io.Response
import kotlinmud.io.Server
import kotlinmud.io.Syntax
import kotlinmud.loader.RoomLoader
import kotlinmud.loader.Tokenizer
import kotlinmud.loader.model.RoomModel
import kotlinmud.mapper.RoomMapper
import kotlinmud.mob.Mob
import kotlinmud.room.Room
import kotlinmud.service.ActionService
import kotlinmud.service.EventService
import kotlinmud.service.FixtureService
import kotlinmud.service.MobService
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

class App(
    private val eventService: EventService,
    private val mobService: MobService,
    private val server: Server
) {
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
        if (client.mob.delay > 0) {
            return
        }
        val request = client.shiftBuffer()
        val response = actionService.run(request)
        eventService.publishRoomMessage(
            createSendMessageToRoomEvent(response.message, mobService.getRoomForMob(request.mob), request.mob, getTarget(response)))
    }

    private fun getTarget(response: Response): Mob? {
        return try {
            response.actionContextList.getResultBySyntax<Mob>(Syntax.MOB_IN_ROOM)
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
            val svc = MobService(instance<EventService>(), loadRooms(), mutableListOf())
            fix.populateWorld(svc)
            svc
        }
    }
}

fun loadRooms(): List<Room> {
    val filename = "areas/midgard/rooms.txt"
    val classLoader = ClassLoader.getSystemClassLoader()
    val tokenizer = Tokenizer(File(classLoader?.getResource(filename)!!.file).readText())
    val roomLoader = RoomLoader(tokenizer)
    val models: MutableList<RoomModel> = mutableListOf()
    while (true) {
        try {
            models.add(roomLoader.load())
        } catch (e: Exception) {
            break
        }
    }
    return RoomMapper(models.toList()).map()
}

package kotlinmud.web

import com.google.gson.Gson
import io.ktor.server.application.call
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinmud.item.service.ItemService
import kotlinmud.mob.service.MobService
import kotlinmud.persistence.dumper.AreaDumperService
import kotlinmud.persistence.model.RoomModel
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Direction
import kotlinmud.room.type.getReverseDirection

class WebServerService(
    private val mobService: MobService,
    private val itemService: ItemService,
    private val roomService: RoomService,
) {
    fun start() {
        embeddedServer(Netty, port = 8000) {
            routing {
                get("/") {
                    call.respondText(Gson().toJson(getHome()))
                }
                post("/room") {
                    val text = call.receiveText()
                    val model = Gson().fromJson(text, RoomModel::class.java)
                    model.id = roomService.getNextAutoId()
                    model.keywords.forEach { pair ->
                        val direction = Direction.valueOf(pair.key.uppercase())
                        val source = roomService.getModel(pair.value.toInt()) as RoomModel
                        source.keywords[getReverseDirection(direction).value] = model.id.toString()
                    }
                    roomService.addModel(model)
                    flush()
                    call.respond(201)
                }
            }
        }.start(wait = false)
    }

    private fun flush() {
        AreaDumperService(roomService, mobService, itemService).dump()
    }

    private fun getHome(): Map<String, Int> {
        return mapOf(
            Pair("mobs", mobService.getMobCount()),
            Pair("rooms", roomService.getRoomCount()),
            Pair("items", itemService.getItemCount()),
        )
    }
}

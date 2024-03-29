package kotlinmud.web

import com.google.gson.GsonBuilder
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.request.receiveText
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinmud.item.service.ItemService
import kotlinmud.mob.service.MobService
import kotlinmud.persistence.dumper.AreaDumperService
import kotlinmud.persistence.model.MobModel
import kotlinmud.persistence.model.RoomModel
import kotlinmud.room.model.Area
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Direction
import kotlinmud.room.type.getReverseDirection

class WebServerService(
    private val mobService: MobService,
    private val itemService: ItemService,
    private val roomService: RoomService,
) {
    private val gson = GsonBuilder().setPrettyPrinting().create()

    fun start() {
        embeddedServer(Netty, port = 8000) {
            routing {
                get("/") {
                    call.respondText(gson.toJson(getHome()))
                }
                get("/area") {
                    call.respondText(gson.toJson(getAllAreas()))
                }
                post("/area") {
                    val area = gson.fromJson(call.receiveText(), Area::class.java)
                    roomService.addArea(area)
                    flush()
                    call.respondText(
                        gson.toJson(area),
                        null,
                        HttpStatusCode.Created,
                    )
                }
                get("/area/{areaId}") {
                    call.respondText(
                        gson.toJson(getArea(call.parameters["areaId"]!!.toInt()))
                    )
                }
                get("/area/{areaId}/room") {
                    call.respondText(
                        gson.toJson(getRoomsForArea(call.parameters["areaId"]!!.toInt()))
                    )
                }
                get("/room/{roomId}") {
                    call.respondText(
                        gson.toJson(getRoom(call.parameters["roomId"]!!.toInt()))
                    )
                }
                post("/room") {
                    val model = gson.fromJson(call.receiveText(), RoomModel::class.java)
                    createRoom(model)
                    call.respondText(
                        gson.toJson(model),
                        null,
                        HttpStatusCode.Created,
                    )
                }
                get("/mob/{mobId}") {
                    call.respondText(
                        gson.toJson(getMob(call.parameters["mobId"]!!.toInt()))
                    )
                }
                post("/mob") {
                    val model = gson.fromJson(call.receiveText(), MobModel::class.java)
                    createMob(model)
                    call.respondText(
                        gson.toJson(model),
                        null,
                        HttpStatusCode.Created,
                    )
                }
            }
        }.start(wait = false)
    }

    private fun flush() {
        AreaDumperService(roomService, mobService, itemService).dump()
    }

    private fun createRoom(model: RoomModel) {
        model.id = roomService.getNextAutoId()
        model.keywords.forEach { pair ->
            val direction = Direction.valueOf(pair.key.uppercase())
            val source = roomService.getModel(pair.value.toInt()) as RoomModel
            source.keywords[getReverseDirection(direction).value] = model.id.toString()
        }
        roomService.addModel(model)
        flush()
    }

    private fun createMob(model: MobModel) {
        model.id = mobService.getNextAutoId()
        mobService.addModel(model)
        flush()
    }

    private fun getMob(mobId: Int): MobModel {
        return mobService.getModel(mobId) as MobModel
    }

    private fun getRoom(roomId: Int): RoomModel {
        return roomService.getModel(roomId) as RoomModel
    }

    private fun getRoomsForArea(areaId: Int): List<RoomModel> {
        val area = roomService.getAreaById(areaId)
        return roomService.findRoomModels(area)
    }

    private fun getArea(areaId: Int): Area {
        return roomService.getAreaById(areaId)
    }

    private fun getHome(): Map<String, Int> {
        return mapOf(
            Pair("mobs", mobService.getMobCount()),
            Pair("rooms", roomService.getRoomCount()),
            Pair("items", itemService.getItemCount()),
        )
    }

    private fun getAllAreas(): List<Area> {
        return roomService.getAllAreas()
    }
}

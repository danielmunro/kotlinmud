package kotlinmud.world.itrias.troy

import kotlinmud.generator.service.SimpleMatrixService
import kotlinmud.room.builder.RoomBuilder
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area

fun createTroyPromenade(roomService: RoomService, connector: Room): Room {
    val builder = RoomBuilder(roomService).also {
        it.area = Area.TroyPromenade
        it.name = "On The Promenade"
        it.description = "foo"
    }
    val matrix = SimpleMatrixService(builder).build(5, 5)

    connect(connector).toRoom(matrix[2][4])

    return matrix[2][0]
}

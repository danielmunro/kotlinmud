package kotlinmud.generator.service

import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.helper.RoomBuilder
import kotlinmud.room.helper.connect
import kotlinmud.room.type.Direction

class SimpleMatrixService(private val builder: RoomBuilder) {
    fun build(length: Int, width: Int): Array<Array<RoomDAO>> {
        val matrix: Array<Array<RoomDAO>> = Array(length) {
            Array(width) {
                builder.build()
            }
        }
        for (y in 0 until length) {
            for (x in 0 until width) {
                if (x + 1 < width) {
                    connect(matrix[y][x]).to(matrix[y][x + 1], Direction.EAST)
                }
                if (y + 1 < length) {
                    connect(matrix[y][x]).to(matrix[y + 1][x], Direction.SOUTH)
                }
            }
        }
        return matrix
    }
}

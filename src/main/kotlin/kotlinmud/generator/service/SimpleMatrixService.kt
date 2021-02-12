package kotlinmud.generator.service

import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.helper.RoomBuilder
import kotlinmud.room.helper.connect

class SimpleMatrixService(private val builder: RoomBuilder) {
    fun build(length: Int, width: Int): Array<Array<RoomDAO>> {
        val matrix: Array<Array<RoomDAO>> = Array(length) {
            Array(width) {
                builder.build()
            }
        }
        for (y in 0 until length) {
            for (x in 0 until width) {
                if (x - 1 > 0) {
                    connect(matrix[y][x - 1]) to matrix[y][x]
                }
                if (y - 1 > 0) {
                    connect(matrix[y - 1][x]) to matrix[y][x]
                }
            }
        }
        return matrix
    }
}

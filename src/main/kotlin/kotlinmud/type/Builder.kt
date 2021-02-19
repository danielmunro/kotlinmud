package kotlinmud.type

import kotlinmud.room.dao.RoomDAO

interface Builder {
    fun room(value: RoomDAO): Builder
    fun build(): Any
}

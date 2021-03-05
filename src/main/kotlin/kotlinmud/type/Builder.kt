package kotlinmud.type

import kotlinmud.room.model.Room

interface Builder {
    fun room(value: Room): Builder
    fun build(): Any
}

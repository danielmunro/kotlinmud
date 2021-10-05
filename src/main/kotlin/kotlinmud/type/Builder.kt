package kotlinmud.type

import kotlinmud.room.model.Room

interface Builder {
    var room: Room?
    fun setFromKeyword(keyword: String, value: String)
    fun build(): Any
}

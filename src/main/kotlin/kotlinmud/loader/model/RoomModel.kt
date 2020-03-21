package kotlinmud.loader.model

import kotlinmud.room.RegenLevel

data class RoomModel(
    override val id: Int,
    val name: String,
    val description: String,
    val regen: RegenLevel,
    val north: String,
    val south: String,
    val east: String,
    val west: String,
    val up: String,
    val down: String
) : Model

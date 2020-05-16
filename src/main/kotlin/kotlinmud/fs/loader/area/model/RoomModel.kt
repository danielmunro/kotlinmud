package kotlinmud.fs.loader.area.model

import kotlinmud.world.BiomeType
import kotlinmud.world.room.RegenLevel

data class RoomModel(
    override val id: Int,
    val name: String,
    val description: String,
    val regen: RegenLevel,
    val isIndoor: Boolean,
    val north: String,
    val south: String,
    val east: String,
    val west: String,
    val up: String,
    val down: String,
    val area: String,
    val biomeType: BiomeType,
    val ownerId: Int
) : Model

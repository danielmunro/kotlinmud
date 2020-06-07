package kotlinmud.fs.loader.area.model

import kotlinmud.room.type.RegenLevel
import kotlinmud.world.BiomeType
import kotlinmud.world.ResourceType

data class RoomModel(
    override val id: Int,
    val name: String,
    val description: String,
    val regen: RegenLevel,
    val isIndoor: Boolean,
    val elevation: Int,
    val north: String,
    val south: String,
    val east: String,
    val west: String,
    val up: String,
    val down: String,
    val area: String,
    val biomeType: BiomeType,
    val resources: List<ResourceType>,
    val ownerId: Int
) : Model

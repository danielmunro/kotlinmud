package kotlinmud.room.model

import com.thinkinglogic.builder.annotation.Builder
import com.thinkinglogic.builder.annotation.DefaultValue
import com.thinkinglogic.builder.annotation.Mutable
import kotlinmud.data.Row
import kotlinmud.item.HasInventory
import kotlinmud.mob.model.Mob
import kotlinmud.room.type.DoorDisposition
import kotlinmud.room.type.RegenLevel
import kotlinmud.world.BiomeType
import kotlinmud.world.ResourceType

@Builder
data class Room(
    override val id: Int,
    val area: String,
    val name: String,
    val description: String,
    val regen: RegenLevel,
    val isIndoor: Boolean,
    val biome: BiomeType,
    @DefaultValue("mutableListOf()") @Mutable val resources: MutableList<ResourceType>,
    @DefaultValue("mutableListOf()") @Mutable val exits: MutableList<Exit>,
    var owner: Mob?
) : Row, HasInventory {

    fun openExits(): List<Exit> {
        return exits.filter { it.door == null || it.door.disposition == DoorDisposition.OPEN }
    }
}

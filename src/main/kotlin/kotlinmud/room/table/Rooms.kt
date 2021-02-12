package kotlinmud.room.table

import kotlinmud.biome.type.BiomeType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.mob.table.Mobs
import kotlinmud.room.type.RegenLevel
import org.jetbrains.exposed.dao.IntIdTable

object Rooms : IntIdTable() {
    val name = varchar("name", 255)
    val area = varchar("area", 50)
    val description = text("description")
    val regenLevel = varchar("regenLevel", 50).default(RegenLevel.NORMAL.toString())
    val isIndoor = bool("isIndoor").default(false)
    val biome = varchar("biome", 50).default(BiomeType.NONE.toString())
    val substrate = varchar("substrate", 50).default(SubstrateType.NONE.toString())
    val elevation = integer("elevation").default(1)
    val maxItems = integer("maxItems").nullable()
    val maxWeight = integer("maxWeight").nullable()
    val canonicalId = varchar("canonicalId", 255).nullable()
    val northId = reference("northId", Rooms).nullable()
    val northDoorId = reference("northDoorId", Doors).nullable()
    val southId = reference("southId", Rooms).nullable()
    val southDoorId = reference("southDoorId", Doors).nullable()
    val eastId = reference("eastId", Rooms).nullable()
    val eastDoorId = reference("eastDoorId", Doors).nullable()
    val westId = reference("westId", Rooms).nullable()
    val westDoorId = reference("westDoorId", Doors).nullable()
    val upId = reference("upId", Rooms).nullable()
    val upDoorId = reference("upDoorId", Doors).nullable()
    val downId = reference("downId", Rooms).nullable()
    val downDoorId = reference("downDoorId", Doors).nullable()
    val ownerId = reference("ownerId", Mobs).nullable()
    val resources = reference("resources", Resources).nullable()
}

package kotlinmud.room.table

import kotlinmud.mob.table.Mobs
import org.jetbrains.exposed.dao.IntIdTable

object Rooms : IntIdTable() {
    val name = varchar("name", 255)
    val area = varchar("area", 50)
    val description = text("description")
    val regenLevel = varchar("regenLevel", 50)
    val isIndoor = bool("isIndoor")
    val biome = varchar("biome", 50)
    val substrate = varchar("substrate", 50)
    val resources = varchar("resources", 255)
    val elevation = integer("elevation")
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
}

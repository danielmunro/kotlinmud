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
    val northId = reference("northId", Rooms)
    val northDoorId = reference("northDoorId", Doors)
    val southId = reference("southId", Rooms)
    val southDoorId = reference("southDoorId", Doors)
    val eastId = reference("eastId", Rooms)
    val eastDoorId = reference("eastDoorId", Doors)
    val westId = reference("westId", Rooms)
    val westDoorId = reference("westDoorId", Doors)
    val upId = reference("upId", Rooms)
    val upDoorId = reference("upDoorId", Doors)
    val downId = reference("downId", Rooms)
    val downDoorId = reference("downDoorId", Doors)
    val ownerId = reference("ownerId", Mobs)
}

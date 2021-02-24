package kotlinmud.player.table

import kotlinmud.room.table.Rooms
import org.jetbrains.exposed.dao.IntIdTable

object MobCards : IntIdTable() {
    val mobName = varchar("mobName", 255)
    val experience = integer("experience").default(0)
    val experiencePerLevel = integer("experiencePerLevel").default(1000)
    val trains = integer("trains").default(0)
    val practices = integer("practices").default(0)
    val bounty = integer("bounty").default(0)
    val sacPoints = integer("sacPoints").default(0)
    val hunger = integer("hunger").default(0)
    val thirst = integer("thirst").default(0)
    val skillPoints = integer("skillPoints").default(0)
    val loggedIn = bool("loggedIn").default(false)
    val respawnRoomId = reference("respawnRoomId", Rooms)
}

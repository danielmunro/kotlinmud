package kotlinmud.player.table

import org.jetbrains.exposed.dao.IntIdTable

object MobCards : IntIdTable() {
    val experience = integer("experience")
    val experiencePerLevel = integer("experiencePerLevel")
    val trains = integer("trains")
    val practices = integer("practices")
    val bounty = integer("bounty")
    val sacPoints = integer("sacPoints")
    val hunger = integer("hunger")
    val thirst = integer("thirst")
    val skillPoints = integer("skillPoints")
}

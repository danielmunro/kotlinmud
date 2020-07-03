package kotlinmud.mob.table

import kotlinmud.attributes.table.Attributes
import kotlinmud.mob.skill.table.Skills
import kotlinmud.mob.skill.table.Skills.references
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Table

object Mobs : IntIdTable() {
    val name = varchar("name", 50)
    val brief = varchar("brief", 255)
    val description = text("description")
    val hp = integer("hp")
    val mana = integer("mana")
    val mv = integer("mv")
    val level = integer("level")
    val race = varchar("race", 50)
    val specialization = varchar("specialization", 50)
    val disposition = varchar("disposition", 50)
    val job = varchar("job", 50)
    val gender = varchar("gender", 50)
    val gold = integer("gold")
    val goldMin = integer("goldMin")
    val goldMax = integer("goldMax")
    val wimpy = integer("wimpy")
    val savingThrows = integer("savingThrows")
    val isNpc = bool("isNpc")
    val route = varchar("route", 255)
    val maxItems = integer("maxItems")
    val maxWeight = integer("maxWeight")
    val rarity = varchar("rarity", 50)
    val attributesId = reference("attributes", Attributes)
    // skills
    // affects
}
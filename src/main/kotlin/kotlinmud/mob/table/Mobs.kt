package kotlinmud.mob.table

import kotlinmud.affect.table.Affects
import kotlinmud.attributes.table.Attributes
import kotlinmud.mob.skill.table.Skills
import org.jetbrains.exposed.dao.IntIdTable

object Mobs : IntIdTable() {
    val canonicalId = uuid("canonicalId")
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
    val goldMin = integer("goldMin").nullable()
    val goldMax = integer("goldMax").nullable()
    val wimpy = integer("wimpy")
    val savingThrows = integer("savingThrows")
    val isNpc = bool("isNpc")
    val route = varchar("route", 255).nullable()
    val lastRoute = integer("lastRoute").nullable()
    val maxItems = integer("maxItems")
    val maxWeight = integer("maxWeight")
    val rarity = varchar("rarity", 50)
    val attributesId = reference("attributes", Attributes)
    val affects = reference("affects", Affects)
    val skills = reference("skills", Skills)
}

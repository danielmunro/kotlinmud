package kotlinmud.mob.table

import kotlinmud.affect.table.Affects
import kotlinmud.attributes.constant.startingHp
import kotlinmud.attributes.constant.startingMana
import kotlinmud.attributes.constant.startingMv
import kotlinmud.attributes.table.Attributes
import kotlinmud.mob.skill.table.Skills
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.Rarity
import kotlinmud.player.table.Players
import org.jetbrains.exposed.dao.IntIdTable

object Mobs : IntIdTable() {
    val emailAddress = varchar("emailAddress", 255)
    val name = varchar("name", 50)
    val brief = varchar("brief", 255)
    val description = text("description")
    val hp = integer("hp").default(startingHp)
    val mana = integer("mana").default(startingMana)
    val mv = integer("mv").default(startingMv)
    val level = integer("level").default(1)
    val experience = integer("experience").default(0)
    val experiencePerLevel = integer("experiencePerLevel").default(1000)
    val race = varchar("race", 50)
    val specialization = varchar("specialization", 50)
    val disposition = varchar("disposition", 50).default(Disposition.STANDING.toString())
    val gender = varchar("gender", 50)
    val wimpy = integer("wimpy").default(0)
    val savingThrows = integer("savingThrows").default(0)
    val route = varchar("route", 255).nullable()
    val lastRoute = integer("lastRoute").nullable()
    val maxItems = integer("maxItems").default(100)
    val maxWeight = integer("maxWeight").default(1000)
    val trains = integer("trains").default(0)
    val practices = integer("practices").default(0)
    val bounty = integer("bounty").default(0)
    val sacPoints = integer("sacPoints").default(0)
    val hunger = integer("hunger").default(0)
    val thirst = integer("thirst").default(0)
    val skillPoints = integer("skillPoints").default(0)
    val loggedIn = bool("loggedIn").default(false)
    val roomId = integer("roomId")
    val attributesId = reference("attributes", Attributes)
    val affects = reference("affects", Affects).nullable()
    val skills = reference("skills", Skills).nullable()
    val playerId = reference("playerId", Players).nullable()
}

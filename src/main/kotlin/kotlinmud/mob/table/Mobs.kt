package kotlinmud.mob.table

import kotlinmud.affect.table.Affects
import kotlinmud.attributes.model.startingHp
import kotlinmud.attributes.model.startingMana
import kotlinmud.attributes.model.startingMv
import kotlinmud.attributes.table.Attributes
import kotlinmud.mob.skill.table.Skills
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.Rarity
import kotlinmud.player.table.MobCards
import kotlinmud.player.table.Players
import org.jetbrains.exposed.dao.IntIdTable

object Mobs : IntIdTable() {
    val name = varchar("name", 50)
    val brief = varchar("brief", 255)
    val description = text("description")
    val hp = integer("hp").default(startingHp)
    val mana = integer("mana").default(startingMana)
    val mv = integer("mv").default(startingMv)
    val level = integer("level").default(1)
    val race = varchar("race", 50)
    val specialization = varchar("specialization", 50).nullable()
    val disposition = varchar("disposition", 50).default(Disposition.STANDING.toString())
    val job = varchar("job", 50).nullable()
    val gender = varchar("gender", 50).nullable()
    val gold = integer("gold").default(0)
    val goldMin = integer("goldMin").nullable()
    val goldMax = integer("goldMax").nullable()
    val wimpy = integer("wimpy").default(0)
    val savingThrows = integer("savingThrows").default(0)
    val isNpc = bool("isNpc")
    val route = varchar("route", 255).nullable()
    val lastRoute = integer("lastRoute").nullable()
    val maxItems = integer("maxItems").default(100)
    val maxWeight = integer("maxWeight").default(1000)
    val rarity = varchar("rarity", 50).default(Rarity.COMMON.toString())
    val attributesId = reference("attributes", Attributes)
    val affects = reference("affects", Affects).nullable()
    val skills = reference("skills", Skills).nullable()
    val playerId = reference("playerId", Players).nullable()
    val mobCardId = reference("mobCardId", MobCards).nullable()
}

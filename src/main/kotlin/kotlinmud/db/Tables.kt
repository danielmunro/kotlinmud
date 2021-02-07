package kotlinmud.db

import kotlinmud.affect.table.Affects
import kotlinmud.attributes.table.Attributes
import kotlinmud.item.table.Items
import kotlinmud.mob.skill.table.Skills
import kotlinmud.mob.table.Fights
import kotlinmud.mob.table.Mobs
import kotlinmud.player.table.FactionScores
import kotlinmud.player.table.MobCards
import kotlinmud.player.table.Players
import kotlinmud.quest.table.Quests
import kotlinmud.room.table.Doors
import kotlinmud.room.table.Resources
import kotlinmud.room.table.Rooms
import kotlinmud.time.table.Times
import org.jetbrains.exposed.dao.IntIdTable

fun getTables(): Array<IntIdTable> {
    return arrayOf(
        Mobs,
        Skills,
        Affects,
        Items,
        Rooms,
        Doors,
        Attributes,
        Players,
        MobCards,
        Times,
        Fights,
        Resources,
        FactionScores,
        Quests,
    )
}

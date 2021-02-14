package kotlinmud.player.repository

import kotlinmud.faction.type.FactionType
import kotlinmud.player.dao.FactionScoreDAO
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.player.table.FactionScores
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun findFactionScoreByType(mobCard: MobCardDAO, factionType: FactionType): FactionScoreDAO? {
    return transaction {
        FactionScores.select {
            FactionScores.mobCardId eq mobCard.id and (FactionScores.faction eq factionType.toString())
        }.firstOrNull()?.let { FactionScoreDAO.wrapRow(it) }
    }
}
package kotlinmud.player.dao

import kotlinmud.faction.type.FactionType
import kotlinmud.player.table.FactionScores
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class FactionScoreDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FactionScoreDAO>(FactionScores)

    var faction by FactionScores.faction.transform(
        { it.toString() },
        { FactionType.valueOf(it) }
    )
    var score by FactionScores.score
    var mobCard by MobCardDAO referencedOn FactionScores.mobCardId
}

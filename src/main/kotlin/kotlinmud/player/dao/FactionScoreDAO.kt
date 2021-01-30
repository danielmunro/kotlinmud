package kotlinmud.player.dao

import kotlinmud.player.table.FactionScores
import kotlinmud.player.table.Players
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class FactionScoreDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FactionScoreDAO>(FactionScores)

    var faction by FactionScores.faction
    var score by FactionScores.score

}
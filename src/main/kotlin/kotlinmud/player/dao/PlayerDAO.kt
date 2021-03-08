package kotlinmud.player.dao

import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.table.Mobs
import kotlinmud.player.table.Players
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class PlayerDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PlayerDAO>(Players)

    var name by Players.name
    var email by Players.email
    var created by Players.created
    var lastOTP by Players.lastOTP
    val mobs by MobDAO optionalReferrersOn Mobs.playerId

    override fun equals(other: Any?): Boolean {
        return if (other is PlayerDAO) other.id.value == id.value else super.equals(other)
    }
}

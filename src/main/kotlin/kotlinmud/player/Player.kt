package kotlinmud.player

import kotlinmud.mob.Mob
import org.joda.time.DateTime

data class Player(
    private val id: Int,
    val email: String,
    val password: String,
    val created: DateTime,
    val mobs: MutableList<Mob>
)

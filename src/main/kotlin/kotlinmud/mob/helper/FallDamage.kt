package kotlinmud.mob.helper

import kotlinmud.mob.constant.FALL_DAMAGE_EXTREME_MULTIPLIER
import kotlinmud.mob.constant.FALL_DAMAGE_HIGH
import kotlinmud.mob.constant.FALL_DAMAGE_LOW
import kotlinmud.mob.constant.FALL_DAMAGE_MEDIUM
import kotlinmud.mob.constant.HEIGHT_DIFFERENCE_HIGH
import kotlinmud.mob.constant.HEIGHT_DIFFERENCE_LOW
import kotlinmud.mob.constant.HEIGHT_DIFFERENCE_MEDIUM
import kotlinmud.mob.constant.MAX_WALKABLE_ELEVATION
import kotlinmud.mob.model.Mob
import org.jetbrains.exposed.sql.transactions.transaction

fun takeDamageFromFall(mob: Mob, elevationChange: Int) {
    transaction {
        mob.hp -= when {
            elevationChange < MAX_WALKABLE_ELEVATION + HEIGHT_DIFFERENCE_LOW -> FALL_DAMAGE_LOW
            elevationChange < MAX_WALKABLE_ELEVATION + HEIGHT_DIFFERENCE_MEDIUM -> FALL_DAMAGE_MEDIUM
            elevationChange < MAX_WALKABLE_ELEVATION + HEIGHT_DIFFERENCE_HIGH -> FALL_DAMAGE_HIGH
            else -> elevationChange * FALL_DAMAGE_EXTREME_MULTIPLIER
        }
    }
}

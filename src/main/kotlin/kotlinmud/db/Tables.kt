package kotlinmud.db

import kotlinmud.player.table.Players
import kotlinmud.time.table.Times
import org.jetbrains.exposed.dao.IntIdTable

fun getTables(): Array<IntIdTable> {
    return arrayOf(
        Players,
        Times,
    )
}

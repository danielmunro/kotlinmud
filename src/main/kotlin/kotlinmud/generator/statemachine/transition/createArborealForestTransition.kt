package kotlinmud.generator.statemachine.transition

import kotlinmud.biome.type.ResourceType
import kotlinmud.helper.math.dice
import kotlinmud.room.dao.ResourceDAO
import kotlinmud.room.repository.findJungleRooms
import org.jetbrains.exposed.sql.transactions.transaction

fun createArborealForestTransition() {
    findJungleRooms().forEach { room ->
        transaction {
            repeat(dice(1, 3) - 1) {
                ResourceDAO.new {
                    type = ResourceType.PINE_TREE
                    name = "a pine tree"
                    this.room = room
                }
            }
        }
    }
}

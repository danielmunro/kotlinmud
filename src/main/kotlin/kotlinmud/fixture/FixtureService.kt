package kotlinmud.fixture

import io.github.serpro69.kfaker.Faker
import kotlinmud.exit.Exit
import kotlinmud.item.Inventory
import kotlinmud.mob.Mob
import kotlinmud.room.Direction
import kotlinmud.room.Room
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class FixtureService {
//    private val faker: Faker = Faker()
//    private val gameService

    fun generateWorld(): List<Room> {
        return transaction {
            val room1 = Room.new {
                uuid = UUID.randomUUID()
                name = "a test room 1"
                description = "a test room is here"
                inventory = Inventory.new {}
            }
            val room2 = Room.new {
                uuid = UUID.randomUUID()
                name = "a test room 2"
                description = "a test room is here"
                inventory = Inventory.new {}
            }
            Exit.new {
                room = room1
                destination = room2
                direction = Direction.NORTH
            }
            Exit.new {
                room = room2
                destination = room1
                direction = Direction.SOUTH
            }
            Room.all().with(Room::exits).toList()
        }
    }
}

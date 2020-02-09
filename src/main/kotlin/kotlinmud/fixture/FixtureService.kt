package kotlinmud.fixture

import io.github.serpro69.kfaker.Faker
import kotlinmud.MobService
import kotlinmud.exit.Exit
import kotlinmud.item.Inventory
import kotlinmud.item.Item
import kotlinmud.mob.Disposition
import kotlinmud.mob.Mob
import kotlinmud.room.Direction
import kotlinmud.room.Room
import kotlinmud.room.oppositeDirection
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class FixtureService {
    private var rooms = 0
    private var mobs = 0
    private var items = 0
    private val faker = Faker()

    fun populateWorld(mobService: MobService) {
        mobService.respawnMobToStartRoom(createMob())
    }

    fun generateWorld(): List<Room> {
        val room1 = createRoom()
        val room2 = createRoom()
        val room3 = createRoom()
        createExit(room1, room2, Direction.NORTH)
        createExit(room1, room3, Direction.SOUTH)
        createItem(transaction { room1.inventory })
        return listOf(room1, room2, room3)
    }

    fun createMob(): Mob {
        mobs++
        return transaction {
            Mob.new {
                name = faker.name.name()
                description = "A test mob is here ($mobs)."
                disposition = Disposition.STANDING
                inventory = Inventory.new{}
            }
        }
    }

    fun createItem(inv: Inventory): Item {
        items++
        return transaction {
            Item.new{
                name = "the helmet of ${faker.ancient.hero()}"
                description = "A test item is here ($items)."
                inventory = inv
            }
        }
    }

    private fun createExit(src: Room, dest: Room, dir: Direction): Exit {
        return transaction {
            // reciprocal direction
            Exit.new {
                room = dest
                destination = src
                direction = oppositeDirection(dir)
            }
            Exit.new {
                room = src
                destination = dest
                direction = dir
            }
        }
    }

    private fun createRoom(): Room {
        rooms++
        return transaction {
            Room.new {
                uuid = UUID.randomUUID()
                name = "test room no. $rooms"
                description = "a test room is here"
                inventory = Inventory.new {}
            }
        }
    }
}

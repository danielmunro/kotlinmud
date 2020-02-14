package kotlinmud.service

import io.github.serpro69.kfaker.Faker
import java.util.UUID
import kotlinmud.exit.ExitEntity
import kotlinmud.item.InventoryEntity
import kotlinmud.item.ItemEntity
import kotlinmud.mob.Disposition
import kotlinmud.mob.MobEntity
import kotlinmud.room.Direction
import kotlinmud.room.RoomEntity
import kotlinmud.room.oppositeDirection
import org.jetbrains.exposed.sql.transactions.transaction

class FixtureService {
    private var rooms = 0
    private var mobs = 0
    private var items = 0
    private val faker = Faker()

    fun populateWorld(mobService: MobService) {
        mobService.respawnMobToStartRoom(createMob())
    }

    fun generateWorld(): List<RoomEntity> {
        val room1 = createRoom()
        val room2 = createRoom()
        val room3 = createRoom()
        createExit(room1, room2, Direction.NORTH)
        createExit(room1, room3, Direction.SOUTH)
        createItem(transaction { room1.inventory })
        return listOf(room1, room2, room3)
    }

    fun createMob(): MobEntity {
        mobs++
        return transaction {
            MobEntity.new {
                name = faker.name.name()
                description = "A test mob is here ($mobs)."
                disposition = Disposition.STANDING.value
                inventory = InventoryEntity.new {}
            }
        }
    }

    fun createItem(inv: InventoryEntity): ItemEntity {
        items++
        return transaction {
            ItemEntity.new {
                name = "the helmet of ${faker.ancient.hero()}"
                description = "A test item is here ($items)."
                inventory = inv
            }
        }
    }

    private fun createExit(src: RoomEntity, dest: RoomEntity, dir: Direction): ExitEntity {
        return transaction {
            // reciprocal direction
            ExitEntity.new {
                room = dest
                destination = src
                direction = oppositeDirection(dir)
            }
            ExitEntity.new {
                room = src
                destination = dest
                direction = dir
            }
        }
    }

    private fun createRoom(): RoomEntity {
        rooms++
        return transaction {
            RoomEntity.new {
                uuid = UUID.randomUUID()
                name = "test room no. $rooms"
                description = "a test room is here"
                inventory = InventoryEntity.new {}
            }
        }
    }
}

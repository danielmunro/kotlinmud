package kotlinmud.service

import io.github.serpro69.kfaker.Faker
import kotlinmud.attributes.*
import kotlinmud.exit.Exit
import kotlinmud.item.Inventory
import kotlinmud.item.Item
import kotlinmud.mob.Disposition
import kotlinmud.mob.Mob
import kotlinmud.room.Direction
import kotlinmud.room.Room
import kotlinmud.room.oppositeDirection

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
        createItem(room1.inventory)
        return listOf(room1, room2, room3)
    }

    fun createMob(): Mob {
        mobs++
        return Mob(
                faker.name.name(),
                "A test mob is here ($mobs).",
                Disposition.STANDING,
                startingHp,
                startingMana,
                startingMv,
                createDefaultMobAttributes(),
                Inventory(),
                Inventory())
    }

    fun createItem(inv: Inventory): Item {
        items++
        val item = Item(
                "the ${faker.cannabis.strains()} of ${faker.ancient.hero()}",
                "A test item is here ($items).")
        inv.items.add(item)
        return item
    }

    private fun createExit(src: Room, dest: Room, dir: Direction): Exit {
        val exit = Exit(dest, dir)
        src.exits.add(exit)
        dest.exits.add(Exit(src, oppositeDirection(dir)))
        return exit
    }

    private fun createRoom(): Room {
        rooms++
        return Room(
                "test room no. $rooms",
                "a test room is here",
                Inventory(),
                mutableListOf())
    }
}

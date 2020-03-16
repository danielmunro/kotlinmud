package kotlinmud.service

import io.github.serpro69.kfaker.Faker
import kotlinmud.item.Inventory
import kotlinmud.item.Item
import kotlinmud.mob.JobType
import kotlinmud.mob.Mob
import kotlinmud.mob.SpecializationType
import kotlinmud.mob.race.Race
import kotlinmud.mob.race.impl.Human

class FixtureService {
    private var mobs = 0
    private var items = 0
    private val faker = Faker()

    fun createMobBuilder(): Mob.Builder {
        mobs++
        return Mob.Builder(mobs, faker.name.name())
    }

    fun createMob(race: Race = Human(), specialization: SpecializationType = SpecializationType.NONE, job: JobType = JobType.NONE): Mob {
        mobs++
        return Mob.Builder(mobs, faker.name.name())
            .setRace(race)
            .setSpecialization(specialization)
            .setJob(job)
            .build()
    }

    fun createItemBuilder(name: String = faker.book.title()): Item.Builder {
        items++
        return Item.Builder(items, name)
            .setDescription("$name is here")
    }

    fun createItem(inv: Inventory): Item {
        items++
        val item = Item.Builder(items, "the ${faker.cannabis.strains()} of ${faker.ancient.hero()}")
            .setDescription("A test item is here ($items).")
            .build()
        inv.items.add(item)
        return item
    }
}

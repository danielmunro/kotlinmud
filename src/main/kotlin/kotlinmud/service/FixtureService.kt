package kotlinmud.service

import io.github.serpro69.kfaker.Faker
import kotlinmud.item.Inventory
import kotlinmud.item.Item
import kotlinmud.item.ItemBuilder
import kotlinmud.item.itemBuilder
import kotlinmud.mob.JobType
import kotlinmud.mob.Mob
import kotlinmud.mob.MobBuilder
import kotlinmud.mob.SpecializationType
import kotlinmud.mob.mobBuilder
import kotlinmud.mob.race.Race
import kotlinmud.mob.race.impl.Human

class FixtureService {
    private var mobs = 0
    private var items = 0
    private val faker = Faker()

    fun createMobBuilder(): MobBuilder {
        mobs++
        return mobBuilder(mobs, faker.name.name())
    }

    fun createMob(race: Race = Human(), specialization: SpecializationType = SpecializationType.NONE, job: JobType = JobType.NONE): Mob {
        mobs++
        return mobBuilder(mobs, faker.name.name())
            .race(race)
            .specialization(specialization)
            .job(job)
            .build()
    }

    fun createItemBuilder(name: String = faker.book.title()): ItemBuilder {
        items++
        return itemBuilder(items, name)
            .description("$name is here")
    }

    fun createItem(inv: Inventory): Item {
        items++
        val item = itemBuilder(items, "the ${faker.cannabis.strains()} of ${faker.ancient.hero()}")
            .description("A test item is here ($items).")
            .build()
        inv.items.add(item)
        return item
    }
}

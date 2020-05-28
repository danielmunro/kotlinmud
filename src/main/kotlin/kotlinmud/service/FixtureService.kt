package kotlinmud.service

import io.github.serpro69.kfaker.Faker
import kotlinmud.item.ItemBuilder
import kotlinmud.item.itemBuilder
import kotlinmud.mob.Mob
import kotlinmud.mob.MobBuilder
import kotlinmud.mob.mobBuilder
import kotlinmud.mob.race.Race
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.type.JobType
import kotlinmud.mob.type.SpecializationType

class FixtureService {
    private var mobs = 0
    private var items = 0
    val faker = Faker()

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
}

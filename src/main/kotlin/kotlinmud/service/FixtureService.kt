package kotlinmud.service

import io.github.serpro69.kfaker.Faker
import kotlinmud.item.itemBuilder
import kotlinmud.item.model.ItemBuilder
import kotlinmud.mob.factory.mobBuilder
import kotlinmud.mob.model.Mob
import kotlinmud.mob.model.MobBuilder
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

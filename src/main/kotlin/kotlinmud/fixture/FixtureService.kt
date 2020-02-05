package kotlinmud.fixture

import io.github.serpro69.kfaker.Faker
import kotlinmud.mob.Mob

class FixtureService {
    private val faker: Faker = Faker()
//    private val gameService

    fun mob(): Mob {
        return Mob.new{
            name = faker.name.name()
            description = "a test fixture"
        }
    }
}

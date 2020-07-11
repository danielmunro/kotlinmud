package kotlinmud.service

import io.github.serpro69.kfaker.Faker
import kotlinmud.mob.dao.MobDAO

class FixtureService {
    val faker = Faker()

    fun createMob(): MobDAO {
        return MobDAO.new {
            name = faker.name.name()
        }
    }
}
